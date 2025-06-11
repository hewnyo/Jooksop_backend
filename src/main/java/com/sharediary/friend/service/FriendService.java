package com.sharediary.friend.service;

import com.sharediary.friend.domain.Friend;
import com.sharediary.friend.dto.FriendResponseDto;
import com.sharediary.friend.repository.FriendRepository;
import com.sharediary.user.domain.User;
import com.sharediary.user.dto.UserResponseDto;
import com.sharediary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // ✅ 친구 추가 (양방향)
    public void addFriend(String requesterUserId, String targetUserId) {
        if (!userRepository.existsByUserId(targetUserId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        // 이미 친구인지 확인
        if (friendRepository.existsByRequesterUserIdAndTargetUserId(requesterUserId, targetUserId)) {
            throw new IllegalArgumentException("이미 친구입니다.");
        }

        // A → B 저장
        friendRepository.save(Friend.builder()
                .requesterUserId(requesterUserId)
                .targetUserId(targetUserId)
                .build());

        // B → A 저장 (역방향도 추가)
        if (!friendRepository.existsByRequesterUserIdAndTargetUserId(targetUserId, requesterUserId)) {
            friendRepository.save(Friend.builder()
                    .requesterUserId(targetUserId)
                    .targetUserId(requesterUserId)
                    .build());
        }
    }

    // ✅ 친구 목록 조회
    public List<FriendResponseDto> getFriends(String requesterUserId) {
        List<Friend> friends = friendRepository.findByRequesterUserId(requesterUserId);
        return friends.stream()
                .map(f -> userRepository.findByUserId(f.getTargetUserId())
                        .map(u -> new FriendResponseDto(u.getUserId(), u.getNickname()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ✅ 친구 삭제 (양방향)
    public void removeFriend(String requesterUserId, String targetUserId) {
        friendRepository.deleteByRequesterUserIdAndTargetUserId(requesterUserId, targetUserId);
        friendRepository.deleteByRequesterUserIdAndTargetUserId(targetUserId, requesterUserId);
    }

    // ✅ ID 정확히 일치하는 유저 검색
    public List<UserResponseDto> searchUsersByExactId(String userId) {
        Optional<User> userOpt = userRepository.findByUserId(userId);
        if (userOpt.isEmpty()) {
            return List.of();  // 빈 리스트 반환
        }
        User user = userOpt.get();
        return List.of(new UserResponseDto(
                user.getId(),
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImageUrl()
        ));
    }
}
