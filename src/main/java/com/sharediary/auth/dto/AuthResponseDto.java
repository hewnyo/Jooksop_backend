package com.sharediary.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private boolean success;
    private String message;
    private String data; // 로그인 시 JWT, ID 찾기 시 userId, 그 외에는 null
}
