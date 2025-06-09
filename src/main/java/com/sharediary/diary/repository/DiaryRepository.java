package com.sharediary.diary.repository;

import com.sharediary.diary.domain.Diary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DiaryRepository extends MongoRepository<Diary, String> {
   @Query("{ '$and': [ { 'date': ?1 }, { '$or': [ { 'userId': ?0 }, { 'taggedUserIds': ?0 } ] } ] }")
    List<Diary> findVisibleDiariesByUserAndDate(String userId, String date);
    List<Diary> findByTaggedUserIdsContaining(String userId);
}
