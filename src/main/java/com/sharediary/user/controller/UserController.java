package com.sharediary.user.controller;

import com.sharediary.user.dto.UserRequestDto;
import com.sharediary.user.dto.UserResponseDto;
import com.sharediary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto dto){
        return ResponseEntity.ok(userService.register(dto));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable String id){
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkDuplicate(@RequestParam String userId){
        return ResponseEntity.ok(userService.isDuplicate(userId));
    }

}
