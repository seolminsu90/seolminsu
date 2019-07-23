package com.seol.interceptor;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.seol.dto.ResponseCode;
import com.seol.exception.custom.UserLoginException;
import com.seol.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        String apiKey = null;
        if (cookies != null && cookies.length > 0) {
            Optional<String> cookie = readCookie(request, "apiKey");
            if (!cookie.isPresent()) {
                throw new UserLoginException(ResponseCode.USER_AUTH_FAIL);
            } else {
                apiKey = cookie.get();
                log.debug("interceptor apiKey : " + apiKey);
                String loginId = JwtUtil.verifyToken(apiKey);
                request.setAttribute("id", loginId);

                return true;
            }
        } else {
            throw new UserLoginException(ResponseCode.USER_AUTH_FAIL);
        }
    }

    public Optional<String> readCookie(HttpServletRequest request, String key) {
        return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName())).map(Cookie::getValue).findAny();
    }
}
