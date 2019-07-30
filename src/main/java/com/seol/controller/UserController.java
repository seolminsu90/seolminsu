package com.seol.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.seol.dto.Response;
import com.seol.dto.UserDto;
import com.seol.entity.User;
import com.seol.service.UserService;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    // 회원 가입
    @PostMapping("/api/user")
    public Response<User> signin(@RequestBody @Valid UserDto userDto) {
        return userService.signin(userDto);
    }

    // 로그인
    @PostMapping("/api/user")
    public Response<User> login(HttpServletResponse response, @RequestBody @Valid UserDto userDto) {
        return userService.login(response, userDto);
    }
}
