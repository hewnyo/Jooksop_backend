package com.sharediary.friend.domain;

<<<<<<< HEAD
import jakarta.persistence.Id;
import lombok.*;
=======
import lombok.*;
import org.springframework.data.annotation.Id;
>>>>>>> hyewon
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "friends")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend {
    @Id
    private String id;
    private String requesterUserId;
    private String targetUserId;

}
