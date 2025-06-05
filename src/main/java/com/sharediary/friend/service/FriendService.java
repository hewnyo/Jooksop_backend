package com.sharediary.friend.service;

import com.sharediary.friend.domain.Friend;
import com.sharediary.friend.dto.FriendResponseDto;
import com.sharediary.friend.repository.FriendRepository;
import com.sharediary.user.dto.UserResponseDto;
import com.sharediary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public void addFriend(String requesterUserId, String targetUserId) {
        if (!userRepository.existsByUserId(targetUserId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        if (friendRepository.existsByRequesterUserIdAndTargetUserId(requesterUserId, targetUserId)) {
            throw new IllegalArgumentException(("이미 친구입니다."));
        }

        friendRepository.save(Friend.builder()
                .requesterUserId(requesterUserId)
                .targetUserId(targetUserId)
                .build());
    }

    public List<FriendResponseDto> getFriends(String requesterUserId){
        List<Friend> friends = friendRepository.findByRequesterUserId(requesterUserId);
        return friends.stream()
                .map(f -> userRepository.findByUserId(f.getTargetUserId())
                        .map(u -> new FriendResponseDto(u.getUserId(), u.getNickname()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    public void removeFriend(String requesterUserId, String targetUserId){
        friendRepository.deleteByRequesterUserIdAndTargetUserId(requesterUserId, targetUserId);
    }

    public List<UserResponseDto> searchUsersByExactId(String userId){
        return userRepository.findByUserId(userId)
                .map(user->List.of(new UserResponseDto(user.getId(),
                        user.getUserId(),
                        user.getNickname(),
                        user.getEmail(),
                        user.getProfileImageUrl())))
                .orElse(Collections.emptyList());
    }
}
