package com.sharediary.auth.service;

import com.sharediary.auth.dto.AuthResponseDto;
import com.sharediary.auth.dto.LoginRequestDto;
import com.sharediary.auth.dto.ResetPasswordRequestDto;
import com.sharediary.auth.dto.SignupRequestDto;

public interface AuthService {

    /**
     * 회원가입 처리
     * @param dto 회원가입 요청 정보
     * @return 결과 메시지 및 성공 여부
     */
    AuthResponseDto signup(SignupRequestDto dto);


    /**
     * 로그인 처리
     * @param dto 로그인 요청 정보
     * @return 결과 메시지 및 JWT 토큰 포함 여부
     */
    AuthResponseDto login(LoginRequestDto dto);

    /**
     * 이메일로 아이디 찾기
     * @param email 사용자 이메일
     * @return 사용자 아이디 또는 오류 메시지
     */
    AuthResponseDto findId(String email);

    /**
     * 비밀번호 재설정 처리
     * @param dto 비밀번호 초기화 요청 정보
     * @return 결과 메시지
     */
    AuthResponseDto resetPassword(ResetPasswordRequestDto dto);


}
