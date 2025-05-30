package com.sharediary.socket.delegate;

public interface DiaryEditDelegate {
    boolean hasEditPermission(String diaryId, String userId);
    void applyEdit(String diaryId, String userId, String content);
}
