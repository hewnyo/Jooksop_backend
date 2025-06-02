package com.sharediary.auth.controller;


import com.sharediary.auth.dto.*;
import com.sharediary.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody SignupRequestDto dto) {
        return ResponseEntity.ok(authService.signup(dto));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto dto) {
        AuthResponseDto res=authService.login(dto);
        if(!res.isSuccess()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        return ResponseEntity.ok(res);
    }

    /**
     * 아이디 찾기
     */
    @PostMapping("/find-id")
    public ResponseEntity<AuthResponseDto> findId(@RequestBody FindIdRequestDto dto) {
        return ResponseEntity.ok(authService.findId(dto.getEmail()));
    }

    /**
     * 비밀번호 재설정
     */
    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponseDto> resetPassword(@RequestBody ResetPasswordRequestDto dto) {
        return ResponseEntity.ok(authService.resetPassword(dto));
    }

}
