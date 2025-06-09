package com.sharediary.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {
    private String userId;
    private String password;
    private String confirmPassword;
    private String nickname;
    private String email;
}
