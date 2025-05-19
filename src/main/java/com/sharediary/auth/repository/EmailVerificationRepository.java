package com.sharediary.auth.repository;

import com.sharediary.auth.domain.EmailVerification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends MongoRepository<EmailVerification, String> {
    Optional<EmailVerification> findByEmailAndCode(String email, String code);
}

