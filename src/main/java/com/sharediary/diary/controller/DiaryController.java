package com.sharediary.diary.controller;

import com.sharediary.diary.dto.DiaryRequestDto;
import com.sharediary.diary.dto.DiaryResponseDto;
import com.sharediary.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/{userId}")
    public ResponseEntity<DiaryResponseDto> write(@PathVariable String userId, @RequestBody DiaryRequestDto dto) {
        return ResponseEntity.ok(diaryService.createDiary(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<DiaryResponseDto>> getByDate(@PathVariable String userId, @RequestParam String date){
        return ResponseEntity.ok(diaryService.getDiariesByDate(userId, LocalDate.parse(date)));
    }
}
