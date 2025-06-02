package com.sharediary.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DiaryResponseDto {
    private String id;
    private String userId;
    private String content;
    private LocalDate date;
    private List<String> taggedUserIds;
}
