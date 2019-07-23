package com.seol.exception;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
