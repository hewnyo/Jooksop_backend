package com.sharediary.auth.repository;

public interface EmailVerificationRepository extends MongoRepository<EmailVerification, String> {
    Optional<EmailVerification> findByEmailAndCode(String email, String code);
}

