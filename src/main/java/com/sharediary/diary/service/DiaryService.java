package com.sharediary.diary.service;

import com.sharediary.diary.domain.Diary;
import com.sharediary.diary.dto.DiaryRequestDto;
import com.sharediary.diary.dto.DiaryResponseDto;
import com.sharediary.diary.repository.DiaryRepository;
import com.sharediary.socket.delegate.DiaryEditDelegate;
import com.sharediary.socket.handler.DiaryWebSocketHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService implements DiaryEditDelegate {
    private final DiaryRepository diaryRepository;
    private final DiaryWebSocketHandler diaryWebSocketHandler;


    @PostConstruct
    public void init() {
        diaryWebSocketHandler.setDelegate(this);
    }

    public DiaryResponseDto createDiary(String userId, DiaryRequestDto dto) {
        Diary diary = Diary.builder()
                .userId(userId)
                .content(dto.getContent())
                .date(dto.getDate())
                .taggedUserIds(dto.getTaggedUserIds())
                .updatedAt(LocalDateTime.now())
                .build();
        Diary saved = diaryRepository.save(diary);
        return new DiaryResponseDto(saved.getId(), saved.getUserId(), saved.getContent(), saved.getDate(), saved.getTaggedUserIds());
    }

    //날짜 기반 검색
    public List<DiaryResponseDto> getDiariesByDate(String userId, LocalDate date) {
        return diaryRepository.findByUserIdAndDate(userId, date).stream()
                .map(d -> new DiaryResponseDto(d.getId(), d.getUserId(), d.getContent(), d.getDate(), d.getTaggedUserIds()))
                .collect(Collectors.toList());
    }

    public void applyEdit(String diaryId, String userId, String newContent) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));

        if (!diary.getUserId().equals(userId) &&
                (diary.getTaggedUserIds() == null || !diary.getTaggedUserIds().contains(userId))) {
            throw new RuntimeException("편집 권한이 없습니다.");
        }

        diary.setContent(newContent);
        diary.setUpdatedAt(LocalDateTime.now());
        diaryRepository.save(diary);
    }

    public boolean hasEditPermission(String diaryId, String userId) {
        Diary diary = diaryRepository.findById(diaryId).orElse(null);
        if (diary == null) return false;
        return diary.getUserId().equals(userId) ||
                (diary.getTaggedUserIds() != null && diary.getTaggedUserIds().contains(userId));
    }

    public void removeTag(String diaryId, String targetUserId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));

        if (diary.getTaggedUserIds() != null && diary.getTaggedUserIds().contains(targetUserId)) {
            diary.getTaggedUserIds().remove(targetUserId);
            diaryRepository.save(diary);
            diaryWebSocketHandler.disconnectUserFromDiary(diaryId, targetUserId);
        }
    }



}
