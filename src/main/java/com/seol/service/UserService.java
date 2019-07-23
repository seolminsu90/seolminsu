package com.seol.service;

import javax.servlet.http.HttpServletResponse;

import com.seol.dto.Response;
import com.seol.dto.UserDto;
import com.seol.entity.User;

public interface UserService {
    public Response<User> signin(UserDto userDto);

    public Response<User> login(HttpServletResponse response, UserDto userDto);
}
