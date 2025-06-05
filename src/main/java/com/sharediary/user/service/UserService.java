package com.sharediary.user.service;

import com.sharediary.friend.dto.FriendResponseDto;
import com.sharediary.user.domain.User;
import com.sharediary.user.dto.UserRequestDto;
import com.sharediary.user.dto.UserResponseDto;
import com.sharediary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserRequestDto dto){
        if(userRepository.existsByUserId(dto.getUserId())){
            throw new IllegalArgumentException("이미 사용중인 ID입니다.");
        }

        String encryptedPassword=passwordEncoder.encode(dto.getPassword());

        User user=User.builder()
                .userId(dto.getUserId())
                .nickname(dto.getNickname())
                .password(encryptedPassword)
                .email(dto.getEmail())
                .build();

        User saved=userRepository.save(user);
        return new UserResponseDto(saved.getId(), saved.getUserId(), saved.getNickname(), saved.getEmail(), saved.getProfileImageUrl());

    }

    public UserResponseDto getMyProfile(String userId) {
        System.out.println("받은 userId: " + userId);

        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            System.out.println("❌ DB에서 해당 유저를 찾지 못함");
            throw new RuntimeException("사용자를 찾을 수 없습니다. userId=" + userId);
        }

        User user = optionalUser.get();
        System.out.println("✅ 유저 찾음: " + user.getNickname());

        return new UserResponseDto(
                user.getId(),
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImageUrl()
        );
    }



    public boolean isDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }


}
