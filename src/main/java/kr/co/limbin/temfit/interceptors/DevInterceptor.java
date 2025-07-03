package kr.co.limbin.temfit.interceptors;

import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.mappers.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class DevInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object signedUserObj = request.getSession().getAttribute("signedUser");
        if (!(signedUserObj instanceof UserEntity)) {
            UserEntity signedUser = this.userMapper.selectByEmail("asdf1234@naver.com");
            request.getSession().setAttribute("signedUser", signedUser);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}