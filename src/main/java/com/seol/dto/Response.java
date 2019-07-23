package com.seol.dto;

import lombok.Data;

@Data
public class Response<T> {
    private T data;
    private String code;
}
