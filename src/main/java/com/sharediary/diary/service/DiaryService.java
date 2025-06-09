package com.sharediary.diary.service;

import com.sharediary.diary.domain.Diary;
import com.sharediary.diary.dto.DiaryRequestDto;
import com.sharediary.diary.dto.DiaryResponseDto;
import com.sharediary.diary.repository.DiaryRepository;
import com.sharediary.friend.repository.FriendRepository;
import com.sharediary.socket.delegate.DiaryEditDelegate;
import com.sharediary.socket.handler.DiaryWebSocketHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DiaryService implements DiaryEditDelegate {

    private final DiaryRepository diaryRepository;
    private final DiaryWebSocketHandler diaryWebSocketHandler;
    private final FriendRepository friendRepository;


    @PostConstruct
    public void init() {
        diaryWebSocketHandler.setDelegate(this);
    }

    /**
     * 다이어리 생성
     */
    public DiaryResponseDto createDiary(String userId, DiaryRequestDto dto) {
        List<String> validTaggedUserIds = Optional.ofNullable(dto.getTaggedUserIds())
                .orElse(Collections.emptyList())
                .stream()
                .filter(taggedId -> canTagFriend(userId, taggedId))
                .distinct()
                .collect(Collectors.toList());

        Diary diary = Diary.builder()
                .userId(userId)
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(dto.getDate())
                .taggedUserIds(validTaggedUserIds)
                .updatedAt(LocalDateTime.now())
                .build();

        Diary saved = diaryRepository.save(diary);

        return toDto(saved);
    }

    /**
     * 날짜 기반 다이어리 조회 (작성자 + 태그된 글)
     */
    public List<DiaryResponseDto> getDiariesByDate(String userId, String date) {
        List<Diary> own = diaryRepository.findByUserIdAndDate(userId, date);
        List<Diary> tagged = diaryRepository.findByTaggedUserIdsContaining(userId).stream()
                .filter(d -> date.equals(d.getDate()))
                .collect(Collectors.toList());

        return Stream.concat(own.stream(), tagged.stream())
                .map(this::toDto)
                .collect(Collectors.toList());
    }



    /**
     * 실시간 편집 적용
     */
    @Override
    public void applyEdit(String diaryId, String userId, String newContent, String newTitle) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));

        if (!hasEditPermission(diaryId, userId)) {
            throw new RuntimeException("편집 권한이 없습니다.");
        }

        diary.setContent(newContent);
        diary.setTitle(newTitle);
        diary.setUpdatedAt(LocalDateTime.now());
        diaryRepository.save(diary);
    }


    /**
     * 편집 권한 확인
     */
    @Override
    public boolean hasEditPermission(String diaryId, String userId) {
        return diaryRepository.findById(diaryId)
                .map(diary -> diary.getUserId().equals(userId)
                        || Optional.ofNullable(diary.getTaggedUserIds()).orElse(Collections.emptyList()).contains(userId))
                .orElse(false);
    }



    /**
     * 친구 여부 확인
     */
    @Override
    public boolean canTagFriend(String userId, String taggedUserId) {
        return friendRepository.existsByRequesterUserIdAndTargetUserId(userId, taggedUserId);
    }

    /**
     * 태그 추가
     */
    @Override
    public void addTag(String diaryId, String taggedUserId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));

        if (!canTagFriend(diary.getUserId(), taggedUserId)) {
            throw new RuntimeException("친구가 아닌 사용자는 태그할 수 없습니다.");
        }

        if (diary.getTaggedUserIds() == null) {
            diary.setTaggedUserIds(new ArrayList<>());
        }

        if (!diary.getTaggedUserIds().contains(taggedUserId)) {
            diary.getTaggedUserIds().add(taggedUserId);
            diaryRepository.save(diary);
        }
    }


    /**
     * 태그 제거
     */
    @Override
    public void removeTag(String diaryId, String targetUserId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));

        List<String> tagged = diary.getTaggedUserIds();
        if (tagged != null && tagged.contains(targetUserId)) {
            tagged.remove(targetUserId);
            diaryRepository.save(diary);
            diaryWebSocketHandler.disconnectUserFromDiary(diaryId, targetUserId);
        }
    }

    /**
     * 단일 일기 조회
     */
    public Diary getDiaryById(String diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("해당 일기를 찾을 수 없습니다."));
    }

    private DiaryResponseDto toDto(Diary diary) {
        return new DiaryResponseDto(
                diary.getId(),
                diary.getUserId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getDate(),
                diary.getTaggedUserIds()
        );
    }

}
