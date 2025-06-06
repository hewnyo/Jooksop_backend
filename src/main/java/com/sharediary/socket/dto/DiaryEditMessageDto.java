package com.sharediary.socket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryEditMessageDto {
    private String type;             // "EDIT", "TAG_ADD", "TAG_REMOVE"
    private String diaryId;          // 다이어리 ID
    private String userId;           // 발신자 userId
    private String title;            // 제목
    private String content;          // 본문
    private int cursorPosition;      // 커서 위치 (선택사항)
    private String taggedUserId;     // 태그 대상 사용자 (TAG_ADD / TAG_REMOVE용)
}
