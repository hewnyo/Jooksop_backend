package com.sharediary.diary.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiaryRequestDto {
    private String title;
    private String content;
    private String date;
    private List<String> taggedUserIds;
}
