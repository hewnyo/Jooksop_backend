package com.sharediary.diary.repository;

import com.sharediary.diary.domain.Diary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiaryRepository extends MongoRepository<Diary, String> {
    List<Diary> findByUserIdAndDate(String userId, LocalDate date);
    List<Diary> findByTaggedUserIdsContaining(String userId);
}
