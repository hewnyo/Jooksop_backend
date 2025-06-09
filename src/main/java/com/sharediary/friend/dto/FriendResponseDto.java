package com.sharediary.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FriendResponseDto {
    private String userId;
    private String nickname;
}
