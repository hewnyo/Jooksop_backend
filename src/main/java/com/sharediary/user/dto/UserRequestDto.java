package com.sharediary.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserRequestDto {

    @Email
    @NotBlank
    private String eamil;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;
}
