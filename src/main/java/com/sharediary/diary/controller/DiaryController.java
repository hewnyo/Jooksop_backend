package com.sharediary.diary.controller;

import com.sharediary.diary.dto.DiaryRequestDto;
import com.sharediary.diary.dto.DiaryResponseDto;
import com.sharediary.diary.service.DiaryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/{userId}")
    public ResponseEntity<DiaryResponseDto> write(@PathVariable String userId, @RequestBody DiaryRequestDto dto) {
        System.out.println("📩 받은 날짜: " + dto.getDate());
        return ResponseEntity.ok(diaryService.createDiary(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getByDate(@PathVariable String userId, @RequestParam String date, HttpServletRequest request) {
        System.out.println("🔍 Authorization: " + request.getHeader("Authorization"));
        LocalDate localDate=LocalDate.parse(date);
        return ResponseEntity.ok(diaryService.getDiariesByDate(userId, date));
    }
}

