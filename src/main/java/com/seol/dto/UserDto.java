package com.seol.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserDto {
    @Size(min = 4, max = 20)
    @NotNull
    private String id;

    @Size(min = 4, max = 20)
    @NotNull
    private String pwd;
}
