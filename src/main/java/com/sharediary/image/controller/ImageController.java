package com.sharediary.image.controller;

import com.sharediary.image.dto.ImageResponseDto;
import com.sharediary.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/{userId}/upload")
    public ResponseEntity<String> upload(@PathVariable String userId,
                                         @RequestParam("file")MultipartFile file){
        try {
             var saved=imageService.uploadImage(userId, file);
             return ResponseEntity.ok("업로드 성공! 파일 ID: "+saved.getFileIdGridFs());
        } catch (IOException e){
            return ResponseEntity.status(500).body("업로드 실패: "+e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ImageResponseDto>> list(@PathVariable String userId){
        return ResponseEntity.ok(imageService.getImagesByUser(userId));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable String fileId){
        try {
            byte[] data=imageService.downloadImage(fileId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileId+"\"")
                    .body(data);
        } catch (IOException e){
            return ResponseEntity.status(404).body(null);
        }
    }
}
