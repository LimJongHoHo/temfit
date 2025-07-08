package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.EmailTokenEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.results.ResultTuple;
import kr.co.limbin.temfit.services.EmailTokenService;
import kr.co.limbin.temfit.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailTokenService emailTokenService;

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex() {
        return "user/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postLogin(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "password", required = false) String password, HttpSession session) {
        ResultTuple<UserEntity> result = this.userService.login(email, password);
        if (result.getResult() == CommonResult.SUCCESS) {
            session.setAttribute("signedUser", result.getPayload());
        }
        JSONObject response = new JSONObject();
        response.put("result", result.getResult().toStringLower());
        return response.toString();
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.TEXT_HTML_VALUE)
    public String getLogout(HttpSession session) {
        session.setAttribute("signedUser", null);
        return "redirect:/main";
    }

    @RequestMapping(value = "/nickname-check", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postNickNameCheck(@RequestParam(value = "nickname") String nickname) {
        Result result = userService.checkNickname(nickname);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());

        return response.toString();
    }

    @RequestMapping(value = "/recover", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getRecover(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser) {
        if (signedUser != null) {
            return "redirect:/";
        }
        return "user/recover";
    }

    @RequestMapping(value = "/recover-email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRecoverEmail(UserEntity user) {
        String email = this.userService.recoverEmail(user);
        JSONObject response = new JSONObject();
        response.put("email", email == null ? "failure" : email);

        return response.toString();
    }

    @RequestMapping(value = "/recover-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRecoverPassword(EmailTokenEntity emailToken, HttpServletRequest request, @RequestParam(value = "password", required = false) String password) {
        emailToken.setUserAgent(request.getHeader("User-Agent"));
        Result result = this.userService.recoverPassword(emailToken, password);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());

        return response.toString();
    }

    @RequestMapping(value = "/recover-password-email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRecoverPasswordEmail(@RequestParam(value = "email") String email, HttpServletRequest request) throws MessagingException {
        String userAgent = request.getHeader("User-Agent");
        ResultTuple<EmailTokenEntity> result = this.userService.sendRecoverPasswordEmail(email, userAgent);
        JSONObject response = new JSONObject();
        response.put("result", result.getResult().toStringLower());
        if (result.getResult() == CommonResult.SUCCESS) {
            response.put("salt", result.getPayload().getSalt());
        }

        return response.toString();
    }

    @RequestMapping(value = "/recover-password-email", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchRecoverPasswordEmail(EmailTokenEntity emailToken, HttpServletRequest request) {
        emailToken.setUserAgent(request.getHeader("User-Agent"));
        Result result = this.emailTokenService.verifyEmailToken(emailToken);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());

        return response.toString();
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getRegister() {
        return "user/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRegister(EmailTokenEntity emailToken, UserEntity user, HttpServletRequest request) {
        emailToken.setUserAgent(request.getHeader("User-Agent"));
        Result result = userService.register(emailToken, user);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());

        return response.toString();
    }

    @RequestMapping(value = "/register-email", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchRegisterEmail(EmailTokenEntity emailToken, HttpServletRequest request) {
        emailToken.setUserAgent(request.getHeader("User-Agent"));
        Result result = this.emailTokenService.verifyEmailToken(emailToken);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());

        return response.toString();
    }

    @RequestMapping(value = "/register-email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRegisterEmail(@RequestParam(value = "email") String email,
            HttpServletRequest request) throws MessagingException {
        String userAgent = request.getHeader("User-Agent");
        ResultTuple<EmailTokenEntity> result = this.userService.sendRegisterEmail(email, userAgent);
        JSONObject response = new JSONObject();
        response.put("result", result.getResult().toStringLower());
        if (result.getResult() == CommonResult.SUCCESS) {
            response.put("salt", result.getPayload().getSalt());
        }

        return response.toString();
    }
}
