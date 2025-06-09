package com.sharediary.diary.repository;

import com.sharediary.diary.domain.Diary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DiaryRepository extends MongoRepository<Diary, String> {

    // 사용자가 작성하거나 태그된 글 중 특정 날짜의 글
    @Query("{ '$and': [ { 'date': ?1 }, { '$or': [ { 'userId': ?0 }, { 'taggedUserIds': ?0 } ] } ] }")
    List<Diary> findVisibleDiariesByUserAndDate(String userId, String date);

    // 태그된 모든 글 (날짜 필터 없이)
    List<Diary> findByTaggedUserIdsContaining(String userId);
}
