package com.sharediary.friend.repository;

import com.sharediary.friend.domain.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRepository extends MongoRepository<Friend, String> {
    List<Friend> findByRequesterUserId(String requesterUserId);
    boolean existsByRequesterUserIdAndTargetUserId(String requesterUserId, String targetUserId);
    void deleteByRequesterUserIdAndTargetUserId(String requesterUserId, String targetUserId);
}
