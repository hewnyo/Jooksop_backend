package com.sharediary.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String userId;
    private String password;
    private String nickname;
    private String email;
}
