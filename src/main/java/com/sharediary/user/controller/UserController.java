package com.sharediary.user.controller;

import com.sharediary.user.dto.UserRequestDto;
import com.sharediary.user.dto.UserResponseDto;
import com.sharediary.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto requestDto){
        return userService.createUser(requestDto);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }
}
