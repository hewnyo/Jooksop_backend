package com.sharediary.diary.service;

import com.sharediary.diary.domain.Diary;
import com.sharediary.diary.dto.DiaryRequestDto;
import com.sharediary.diary.dto.DiaryResponseDto;
import com.sharediary.diary.repository.DiaryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public DiaryResponseDto createDiary(String userId, DiaryRequestDto dto){
        Diary diary = Diary.builder()
                .userId(userId)
                .content(dto.getContent())
                .date(dto.getDate())
                .taggedUserIds(dto.getTaggedUserIds())
                .build();
        Diary saved=diaryRepository.save(diary);
        return new DiaryResponseDto(saved.getId(), saved.getUserId(), saved.getContent(), saved.getDate(), saved.getTaggedUserIds());
    }

    //날짜 기반 검색
    public List<DiaryResponseDto> getDiariesByDate(String userId, LocalDate date){
        return diaryRepository.findByUserIdAndDate(userId, date).stream()
                .map(d->new DiaryResponseDto(d.getId(), d.getUserId(), d.getContent(), d.getDate(), d.getTaggedUserIds()))
                .collect(Collectors.toList());
    }
}
