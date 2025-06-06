package com.sharediary.socket.delegate;

public interface DiaryEditDelegate {
    boolean hasEditPermission(String diaryId, String userId);
    void applyEdit(String diaryId, String userId, String content);
    boolean canTagFriend(String userId, String taggedUserId);
    void addTag(String diaryId, String taggedUserId);
    void removeTag(String diaryId, String taggedUserId);
}
