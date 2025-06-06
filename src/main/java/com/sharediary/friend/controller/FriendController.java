package com.sharediary.friend.controller;

import com.sharediary.friend.dto.FriendRequestDto;
import com.sharediary.friend.dto.FriendResponseDto;
import com.sharediary.friend.service.FriendService;
import com.sharediary.user.dto.UserResponseDto;
import com.sharediary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    @PostMapping("/{requesterUserId}")
    public ResponseEntity<Void> addFriend(@PathVariable String requesterUserId, @RequestBody FriendRequestDto dto){
        friendService.addFriend(requesterUserId, dto.getTargetUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{requesterUserId}")
    public ResponseEntity<List<FriendResponseDto>> getFriends(@PathVariable String requesterUserId){
        return ResponseEntity.ok(friendService.getFriends(requesterUserId));
    }

    @DeleteMapping("/{requesterUserId}")
    public ResponseEntity<Void> removeFriend(@PathVariable String requesterUserId, @RequestBody FriendRequestDto dto){
        friendService.removeFriend(requesterUserId, dto.getTargetUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchById(@RequestParam String userId){
        List<UserResponseDto> result = friendService.searchUsersByExactId(userId);
        System.out.println("🔁 응답 데이터: " + result);
        return ResponseEntity.ok(result);
    }

}
