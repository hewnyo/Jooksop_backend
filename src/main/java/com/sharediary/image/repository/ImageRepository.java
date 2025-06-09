package com.sharediary.image.repository;

import com.sharediary.image.domain.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageRepository extends MongoRepository<Image, String> {
    List<Image> findByuserId(String userId);
}
