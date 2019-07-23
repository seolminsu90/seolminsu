package com.seol.dto;

public enum ResponseCode {
    OK("0000"), FAIL("1111"), USER_EXISTS("0100"), USER_AUTH_FAIL("0101"), EMPTY_LIST("0200"), TOKEN_ERROR("0300");

    private String value;

    ResponseCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
