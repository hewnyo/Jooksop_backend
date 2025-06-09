package com.sharediary.diary.repository;

import com.sharediary.diary.domain.DiarySnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DiarySnapshotRepository extends MongoRepository<DiarySnapshot, String> {
    Optional<DiarySnapshot> findTopByDiaryIdAndUserIdOrderByCreatedAtDesc(String diaryId, String userId);
}

