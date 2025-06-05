package com.sharediary.user.controller;

import com.sharediary.auth.jwt.JwtProvider;
import com.sharediary.user.dto.UserRequestDto;
import com.sharediary.user.dto.UserResponseDto;
import com.sharediary.user.repository.UserRepository;
import com.sharediary.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto dto){
        return ResponseEntity.ok(userService.register(dto));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable String id){
        return ResponseEntity.ok(userService.getMyProfile(id));
    }

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkDuplicate(@RequestParam String userId){
        return ResponseEntity.ok(userService.isDuplicate(userId));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }



}
