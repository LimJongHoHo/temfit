package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.EmailTokenEntity;
import kr.co.limbin.temfit.mappers.EmailTokenMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.results.email_token.VerifyEmailTokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailTokenService {
    public static boolean isCodeValid(String code) {
        return code != null && code.matches("^(\\d{6})$");
    }

    public static boolean isSaltValid(String salt) {
        return salt != null && salt.matches("^([\\da-zA-Z]{128})$");
    }

    private final EmailTokenMapper emailTokenMapper;

    public Result verifyEmailToken(EmailTokenEntity emailToken) {
        if (emailToken == null
                || !UserService.isEmailValid(emailToken.getEmail())
                || !EmailTokenService.isCodeValid(emailToken.getCode())
                || !EmailTokenService.isSaltValid(emailToken.getSalt())) {
            return CommonResult.FAILURE;
        }

        EmailTokenEntity dbEmailToken = this.emailTokenMapper.selectByEmailAndCodeAndSalt(emailToken.getEmail(), emailToken.getCode(), emailToken.getSalt());

        if (dbEmailToken == null
                || !dbEmailToken.getUserAgent().equals(emailToken.getUserAgent())
                || dbEmailToken.isUsed()) {
            return CommonResult.FAILURE;
        }

        if (dbEmailToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            return VerifyEmailTokenResult.FAILURE_EXPIRED;
        }

        dbEmailToken.setUsed(true);

        return this.emailTokenMapper.update(dbEmailToken) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }
}
