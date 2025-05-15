package com.sharediary.auth.service;

import com.sharediary.auth.dto.AuthResponseDto;
import com.sharediary.auth.dto.LoginRequestDto;
import com.sharediary.auth.dto.SignupRequestDto;

public interface AuthService {
    AuthResponseDto signup(SignupRequestDto dto);
    AuthResponseDto login(LoginRequestDto dto);
    boolean verifyEmail(String Email, String code);
}
