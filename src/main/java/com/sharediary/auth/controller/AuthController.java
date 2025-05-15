package com.sharediary.auth.controller;

import com.sharediary.auth.dto.AuthResponseDto;
import com.sharediary.auth.dto.LoginRequestDto;
import com.sharediary.auth.dto.SignupRequestDto;
import com.sharediary.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody SignupRequestDto dto){
        return ResponseEntity.ok(authService.signup(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Boolean> verifyEmail(@RequestParam String email, @RequestParam String code){
        return ResponseEntity.ok(authService.verifyEmail(email, code));
    }

}
