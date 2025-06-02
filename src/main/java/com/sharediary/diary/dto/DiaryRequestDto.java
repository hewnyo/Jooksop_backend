package com.sharediary.diary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DiaryRequestDto {
    private String content;
    private String date;
    private List<String> taggedUserIds;
}
