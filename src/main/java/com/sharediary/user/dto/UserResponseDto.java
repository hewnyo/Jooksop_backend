package com.sharediary.user.dto;

import com.sharediary.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String id;
    private String userId;
    private String nickname;
    private String email;
    private String profileImageUrl;

}

