package com.sharediary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private String id;
    private String userId;
    private String nickname;
    private String email;
    private String profileImageUrl;
}

