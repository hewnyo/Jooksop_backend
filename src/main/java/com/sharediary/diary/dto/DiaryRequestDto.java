package com.sharediary.diary.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DiaryRequestDto {
    private String content;
    private LocalDate date;
    private List<String> taggedUserIds;
}
