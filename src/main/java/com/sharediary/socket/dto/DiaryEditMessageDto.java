package com.sharediary.socket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryEditMessageDto {
    private String diaryId;
    private String content;
    private int cursorPosition;
}
