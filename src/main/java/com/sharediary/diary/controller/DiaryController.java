package com.sharediary.diary.controller;

import com.sharediary.diary.domain.Diary;
import com.sharediary.diary.dto.DiaryRequestDto;
import com.sharediary.diary.dto.DiaryResponseDto;
import com.sharediary.diary.service.DiaryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<DiaryResponseDto> write(@RequestBody DiaryRequestDto dto) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(diaryService.createDiary(userId, dto));
    }

    @GetMapping
    public ResponseEntity<List<DiaryResponseDto>> getByDate(@RequestParam String date) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(diaryService.getDiariesByDate(userId, date));
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryResponseDto> getDiaryById(@PathVariable String diaryId) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Diary diary = diaryService.getDiaryById(diaryId);

        // 권한 체크
        if (!diaryService.hasEditPermission(diaryId, userId)) {
            return ResponseEntity.status(403).build(); // 권한 없음
        }

        return ResponseEntity.ok(new DiaryResponseDto(
                diary.getId(),
                diary.getUserId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getDate(),
                diary.getTaggedUserIds()
        ));
    }

}
