package com.sharediary.image.domain;

<<<<<<< HEAD
import jakarta.persistence.Id;
=======
>>>>>>> hyewon
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
=======
import org.springframework.data.annotation.Id;
>>>>>>> hyewon
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "images_meta")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    private String id;
    private String userId;
    private String originalFileName;
    private String fileIdGridFs;
    private LocalDate uploadDate;
}
