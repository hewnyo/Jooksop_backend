package com.sharediary.auth.service;

import com.sharediary.auth.dto.AuthResponseDto;
import com.sharediary.auth.dto.LoginRequestDto;
import com.sharediary.auth.dto.SignupRequestDto;
import com.sharediary.user.repository.UserRepository;

public class AuthServiceImpl {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JwtProvider jwtProvider;


    public AuthResponseDto signup(SignupRequestDto dto){

    }

    public AuthResponseDto login(LoginRequestDto dto){

    }

    public boolean verifyEmail(String email, String code){

    }

}
