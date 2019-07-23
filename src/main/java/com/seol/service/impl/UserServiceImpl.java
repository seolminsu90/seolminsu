package com.seol.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seol.dto.Response;
import com.seol.dto.ResponseCode;
import com.seol.dto.UserDto;
import com.seol.entity.User;
import com.seol.exception.custom.UserLoginException;
import com.seol.repository.UserRepository;
import com.seol.service.UserService;
import com.seol.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    @Override
    public Response<User> signin(UserDto userDto) {
        Response<User> response = new Response<>();
        User user = new User();
        Optional<User> exists = repository.findById(userDto.getId());
        if (exists.isPresent()) {
            throw new UserLoginException(ResponseCode.USER_EXISTS);// 이미 가입된 아이디입니다.
        } else {
            try {
                user.setId(userDto.getId());
                user.setPwd(sha256(userDto.getPwd()));
                repository.save(user); // 회원 가입 처리
            } catch (NoSuchAlgorithmException e) {
                throw new UserLoginException(ResponseCode.FAIL);
            }

            response.setData(user);
            response.setCode(ResponseCode.OK.getValue()); // 성공
        }
        return response;
    }

    @Override
    public Response<User> login(HttpServletResponse httpResponse, UserDto userDto) {
        Response<User> response = new Response<>();
        Optional<User> exists = repository.findById(userDto.getId());
        if (exists.isPresent()) {
            User user = exists.get();
            try {
                if (sha256(userDto.getPwd()).equals(user.getPwd())) {
                    String apiKey = JwtUtil.createToken(user.getId());
                    log.debug("apiKey : " + apiKey);
                    Cookie setCookie = new Cookie("apiKey", apiKey); // ApiKey 쿠키 저장
                    setCookie.setMaxAge(60 * 60 * 24 * 30);
                    setCookie.setPath("/");
                    httpResponse.addCookie(setCookie);

                    response.setData(user);
                    response.setCode(ResponseCode.OK.getValue()); // 성공
                } else {
                    throw new UserLoginException(ResponseCode.USER_AUTH_FAIL); // 아이디 또는 비밀번호가 일치하지 않습니다.
                }
            } catch (NoSuchAlgorithmException e) {
                throw new UserLoginException(ResponseCode.FAIL);
            }
        } else {
            throw new UserLoginException(ResponseCode.USER_AUTH_FAIL); // 아이디 또는 비밀번호가 일치하지 않습니다.
        }
        return response;
    }

    public static String sha256(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());

        StringBuilder builder = new StringBuilder();
        for (byte b : md.digest()) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
