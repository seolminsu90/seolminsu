package com.seol.exception.custom;

import com.seol.dto.ResponseCode;

public class UserLoginException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserLoginException(ResponseCode code) {
        super(code.getValue());
    }
}
