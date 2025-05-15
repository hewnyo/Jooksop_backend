package com.sharediary.auth.domain;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "email_verifications")
public class EmailVerification {

    @Id
    private String id;
    private String email;
    private String code;
    private LocalDateTime expiresAt;
}
