package com.sharediary.user.service;

import com.sharediary.user.domain.User;
import com.sharediary.user.dto.UserRequestDto;
import com.sharediary.user.dto.UserResponseDto;
import com.sharediary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto register(UserRequestDto dto){
        if(userRepository.existsByUserId(dto.getUserId())){
            throw new IllegalArgumentException("이미 사용중인 ID입니다.");
        }

        User user=User.builder()
                .userId(dto.getUserId())
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .build();

        User saved=userRepository.save(user);
        return new UserResponseDto(saved.getId(), saved.getUserId(), saved.getNickname(), saved.getEmail(), saved.getProfileImageUrl());

    }

    public UserResponseDto getProfile(String id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("사용자를 찾을 수 없습니다."));
        return new UserResponseDto(user.getId(), user.getUserId(), user.getNickname(), user.getEmail(), user.getProfileImageUrl());

    }

    public boolean isDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }


}
