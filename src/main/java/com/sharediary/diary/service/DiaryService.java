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
import java.util.List;
import java.util.stream.Collectors;

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
        Diary diary = Diary.builder()
                .userId(userId)
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(dto.getDate())
                .taggedUserIds(dto.getTaggedUserIds())
                .updatedAt(LocalDateTime.now())
                .build();

        Diary saved = diaryRepository.save(diary);

        return new DiaryResponseDto(
                saved.getId(),
                saved.getUserId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getDate(),
                saved.getTaggedUserIds()
        );
    }

    /**
     * 날짜 기반 다이어리 조회
     */
    public List<DiaryResponseDto> getDiariesByDate(String userId, String date) {
        return diaryRepository.findByUserIdAndDate(userId, date).stream()
                .map(d -> new DiaryResponseDto(
                        d.getId(),
                        d.getUserId(),
                        d.getTitle(),
                        d.getContent(),
                        d.getDate(),
                        d.getTaggedUserIds()))
                .collect(Collectors.toList());
    }


    /**
     * 본문 수정 처리 (실시간 WebSocket)
     */
    @Override
    public void applyEdit(String diaryId, String userId, String newContent) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));

        if (!hasEditPermission(diaryId, userId)) {
            throw new RuntimeException("편집 권한이 없습니다.");
        }

        diary.setContent(newContent);
        diary.setUpdatedAt(LocalDateTime.now());
        diaryRepository.save(diary);
    }

    /**
     * 편집 권한 확인 (WebSocket 연결 허용 여부)
     */
    @Override
    public boolean hasEditPermission(String diaryId, String userId) {
        Diary diary = diaryRepository.findById(diaryId).orElse(null);
        if (diary == null) return false;
        return diary.getUserId().equals(userId)
                || (diary.getTaggedUserIds() != null && diary.getTaggedUserIds().contains(userId));
    }



    /**
     * 친구 여부 확인 (태그 가능 여부)
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

        if (diary.getTaggedUserIds() == null) {
            diary.setTaggedUserIds(new ArrayList<>());
        }

        if (!diary.getTaggedUserIds().contains(taggedUserId)) {
            diary.getTaggedUserIds().add(taggedUserId);
            diaryRepository.save(diary);
        }

    }


    @Override
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
