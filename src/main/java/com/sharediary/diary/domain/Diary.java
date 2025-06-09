package com.sharediary.diary.domain;

<<<<<<< HEAD
import jakarta.persistence.Id;
import lombok.*;
=======
import lombok.*;
import org.springframework.data.annotation.Id;
>>>>>>> hyewon
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "diaries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diary {
    @Id
    private String id;

    private String userId;
    private String title;
    private String content;
    private String date;

    @Field("taggedUserIds")
    private List<String> taggedUserIds;

    private LocalDateTime updatedAt;
}
