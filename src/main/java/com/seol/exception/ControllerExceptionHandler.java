package com.seol.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.seol.exception.custom.UserLoginException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserLoginException.class)
    protected ExceptionResponse exceptions(HttpServletRequest request, Exception e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode(e.getMessage());
        return response;
    }
}
