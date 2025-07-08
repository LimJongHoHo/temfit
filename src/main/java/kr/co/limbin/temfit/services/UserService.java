package kr.co.limbin.temfit.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.limbin.temfit.entities.EmailTokenEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.mappers.EmailTokenMapper;
import kr.co.limbin.temfit.mappers.UserMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.results.ResultTuple;
import kr.co.limbin.temfit.results.user.LoginResult;
import kr.co.limbin.temfit.results.user.RegisterResult;
import kr.co.limbin.temfit.utils.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private static EmailTokenEntity generateEMailToken(String email, String userAgent, int expMin) {
        String code = RandomStringUtils.randomNumeric(6);
        String salt = RandomStringUtils.randomAlphanumeric(128);
        return UserService.generateEMailToken(email, userAgent, code, salt, expMin);
    }

    private static EmailTokenEntity generateEMailToken(String email, String userAgent, String code, String salt, int expMin) {
        EmailTokenEntity emailToken = new EmailTokenEntity();
        emailToken.setEmail(email);
        emailToken.setCode(code);
        emailToken.setSalt(salt);
        emailToken.setUserAgent(userAgent);
        emailToken.setUsed(false);
        emailToken.setCreatedAt(LocalDateTime.now());
        emailToken.setExpiredAt(LocalDateTime.now().plusMinutes(expMin));
        return emailToken;
    }

    public static boolean isBirthValid(String input) {
        return input != null && input.matches("^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[1-2]|[0-9]|3[0-1])$");
    }

    public static boolean isContactSecondValid(String input) {
        return input != null && input.matches("^(\\d{3,4})$");
    }

    public static boolean isContactThirdValid(String input) {
        return input != null && input.matches("^(\\d{4})$");
    }

    public static boolean isEmailValid(String input) {
        return input != null && input.matches("^(?=.{8,50}$)([\\da-z\\-_.]{4,})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2,3})?$");
    }

    public static boolean isNameValid(String input) {
        return input != null && input.matches("^([가-힣]{2,5})$");
    }

    public static boolean isNicknameValid(String input) {
        return input != null && input.matches("^([\\da-zA-Z가-힣]{2,10})$");
    }

    public static boolean isPasswordValid(String input) {
        return input != null && input.matches("^([\\da-zA-Z`~!@#$%^&*()-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,50})$");
    }

    private final EmailTokenMapper emailTokenMapper;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final UserMapper userMapper;

    public Result checkNickname(String nickname) {
        if (!UserService.isNicknameValid(nickname)) {
            return CommonResult.FAILURE;
        }
        return this.userMapper.selectCountByNickname(nickname) > 0 ? CommonResult.FAILURE_DUPLICATE : CommonResult.SUCCESS;
    }

    public ResultTuple<UserEntity> login(String email, String password) {
        if (!UserService.isEmailValid(email) || !UserService.isPasswordValid(password)) {
            return ResultTuple.<UserEntity>builder().result(CommonResult.FAILURE).build();
        }

        UserEntity dbuser = this.userMapper.selectByEmail(email);

        if (dbuser == null || dbuser.isDeleted()) {
            return ResultTuple.<UserEntity>builder().result(CommonResult.FAILURE).build();
        }

       if (!dbuser.getPassword().equals(CryptoUtils.hashSha512(password))) {
            return ResultTuple.<UserEntity>builder().result(CommonResult.FAILURE).build();
        }

        if (dbuser.isSuspended()) {
            return ResultTuple.<UserEntity>builder().result(LoginResult.FAILURE_SUSPENDED).build();
        }

        return ResultTuple.<UserEntity>builder().result(CommonResult.SUCCESS).payload(dbuser).build();
    }

    public String recoverEmail(UserEntity user) {
        if (user == null) {
            return null;
        }
        return this.userMapper.selectEmail(user);
    }

    public Result recoverPassword(EmailTokenEntity emailToken, String newPassword) {
        if (emailToken == null
                || !UserService.isEmailValid(emailToken.getEmail())
                || !EmailTokenService.isCodeValid(emailToken.getCode())
                || !EmailTokenService.isSaltValid(emailToken.getSalt())) {
            return CommonResult.FAILURE;
        }

        if (!UserService.isPasswordValid(newPassword)) {
            return CommonResult.FAILURE;
        }

        EmailTokenEntity dbEmailToken = this.emailTokenMapper.selectByEmailAndCodeAndSalt(emailToken.getEmail(), emailToken.getCode(), emailToken.getSalt());

        if (dbEmailToken == null || !dbEmailToken.getUserAgent().equals(emailToken.getUserAgent()) || !dbEmailToken.isUsed()) {
            return CommonResult.FAILURE;
        }

        UserEntity dbUser = this.userMapper.selectByEmail(emailToken.getEmail());

        if (dbUser == null || dbUser.isDeleted() || dbUser.isSuspended()) {
            return CommonResult.FAILURE;
        }

            dbUser.setPassword(CryptoUtils.hashSha512(newPassword));
        dbUser.setModifiedAt(LocalDateTime.now());

        return this.userMapper.update(dbUser) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public ResultTuple<EmailTokenEntity> sendRecoverPasswordEmail(String email, String userAgent) throws MessagingException {
        if (!UserService.isEmailValid(email) || userAgent == null) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }

        UserEntity dbUser = this.userMapper.selectByEmail(email);

        if (this.userMapper.selectCountByEmail(email) == 0) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE_ABSENT).build();
        }

        if (dbUser == null || dbUser.isDeleted()) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }

        EmailTokenEntity emailToken = UserService.generateEMailToken(email, userAgent, 10);
        if (this.emailTokenMapper.insert(emailToken) < 1) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }
        Context context = new Context();
        context.setVariable("code", emailToken.getCode());
        String mailText = this.springTemplateEngine.process("user/recoverPasswordEmail", context);
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("kkyybb0126@gmail.com");
        mimeMessageHelper.setTo(emailToken.getEmail());
        mimeMessageHelper.setSubject("[Temfit] 비밀번호 재설정 인증번호");
        mimeMessageHelper.setText(mailText, true);
        this.javaMailSender.send(mimeMessage);

        return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.SUCCESS).payload(emailToken).build();
    }

    public ResultTuple<EmailTokenEntity> sendRegisterEmail(String email, String userAgent) throws MessagingException {
        if (!UserService.isEmailValid(email) || userAgent == null) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }

        if (this.userMapper.selectCountByEmail(email) > 0) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE_DUPLICATE).build();
        }

        EmailTokenEntity emailToken = UserService.generateEMailToken(email, userAgent, 10);
        if (this.emailTokenMapper.insert(emailToken) < 1) {
            return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.FAILURE).build();
        }
        Context context = new Context();
        context.setVariable("code", emailToken.getCode());
        String mailText = this.springTemplateEngine.process("user/registerEmail", context);
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("kkyybb0126@gmail.com");
        mimeMessageHelper.setTo(emailToken.getEmail());
        mimeMessageHelper.setSubject("[Temfit] 회원가입 인증번호");
        mimeMessageHelper.setText(mailText, true);
        this.javaMailSender.send(mimeMessage);

        return ResultTuple.<EmailTokenEntity>builder().result(CommonResult.SUCCESS).payload(emailToken).build();
    }

    public Result register(EmailTokenEntity emailToken, UserEntity user) {
        if (emailToken == null || user == null
                || user.getBirth() == null
                || user.getGender() == null
                || user.getContactMvnoCode() == null
                || user.getContactFirst() == null
                || user.getAddressPostal() == null
                || user.getAddressPrimary() == null
                || user.getAddressSecondary() == null
                || (!user.getGender().equals("M") && !user.getGender().equals("F"))
                || !UserService.isEmailValid(emailToken.getEmail())
                || !EmailTokenService.isCodeValid(emailToken.getCode())
                || !EmailTokenService.isSaltValid(emailToken.getSalt())
                || !UserService.isEmailValid(user.getEmail())
                || !UserService.isPasswordValid(user.getPassword())
                || !UserService.isNicknameValid(user.getNickname())
                || !UserService.isNameValid(user.getName())
                || !UserService.isContactSecondValid(user.getContactSecond())
                || !UserService.isContactThirdValid(user.getContactThird())) {
            return CommonResult.FAILURE;
        }

        EmailTokenEntity dbEmailToken = this.emailTokenMapper.selectByEmailAndCodeAndSalt(emailToken.getEmail(), emailToken.getCode(), emailToken.getSalt());

        if (dbEmailToken == null
                || !dbEmailToken.isUsed()
                || !dbEmailToken.getUserAgent().equals(emailToken.getUserAgent())) {
            return CommonResult.FAILURE;
        }

        if (this.userMapper.selectCountByEmail(user.getEmail()) > 0) {
            return RegisterResult.FAILURE_DUPLICATE_EMAIL;
        }
        if (this.userMapper.selectCountByNickname(user.getNickname()) > 0) {
            return RegisterResult.FAILURE_DUPLICATE_NICKNAME;
        }
        if (this.userMapper.selectCountByContact(user.getContactFirst(), user.getContactSecond(), user.getContactThird()) > 0) {
            return RegisterResult.FAILURE_DUPLICATE_CONTACT;
        }
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        user.setAdmin(false);
        user.setDeleted(false);
        user.setSuspended(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());

        return this.userMapper.insert(user) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }
}
