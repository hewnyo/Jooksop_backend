package com.sharediary.diary.domain;

<<<<<<< HEAD
import jakarta.persistence.Id;
import lombok.*;
=======
import lombok.*;
import org.springframework.data.annotation.Id;
>>>>>>> hyewon
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "diary_snapshots")
public class DiarySnapshot {

    @Id
    private String id;

    private String diaryId;
    private String userId; // 태그가 삭제된 사용자
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
