package com.sharediary.auth.service;

import com.sharediary.auth.dto.AuthResponseDto;
import com.sharediary.auth.dto.LoginRequestDto;
import com.sharediary.auth.dto.ResetPasswordRequestDto;
import com.sharediary.auth.dto.SignupRequestDto;
import com.sharediary.auth.jwt.JwtProvider;
import com.sharediary.user.domain.User;
import com.sharediary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDto signup(SignupRequestDto dto) {
        if (userRepository.existsByUserId(dto.getUserId())) {
            return new AuthResponseDto(false, "이미 사용 중인 아이디입니다.", null);
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return new AuthResponseDto(false, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", null);
        }

        User user = User.builder()
                .userId(dto.getUserId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .build();

        userRepository.save(user);
        return new AuthResponseDto(true, "회원가입 성공", null);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {
        return userRepository.findByUserId(dto.getUserId())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtProvider.createToken(user.getUserId(), user.getNickname());
                    return new AuthResponseDto(true, "로그인 성공", token);
                })
                .orElseGet(() -> new AuthResponseDto(false, "ID 또는 비밀번호가 일치하지 않습니다.", null));
    }

    @Override
    public AuthResponseDto findId(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new AuthResponseDto(true, "아이디 찾기 성공", user.getUserId()))
                .orElseGet(() -> new AuthResponseDto(false, "등록되지 않은 이메일입니다.", null));
    }

    @Override
    public AuthResponseDto resetPassword(ResetPasswordRequestDto dto) {
        return userRepository.findByUserId(dto.getUserId())
                .filter(user -> user.getEmail().equals(dto.getEmail()))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
                    userRepository.save(user);
                    return new AuthResponseDto(true, "비밀번호 재설정 완료", null);
                })
                .orElseGet(() -> new AuthResponseDto(false, "사용자 정보가 일치하지 않습니다.", null));
    }

}