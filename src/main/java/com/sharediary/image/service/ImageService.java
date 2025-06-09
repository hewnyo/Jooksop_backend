package com.sharediary.image.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sharediary.image.domain.Image;
import com.sharediary.image.dto.ImageResponseDto;
import com.sharediary.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    public Image uploadImage(String userId, @org.jetbrains.annotations.NotNull MultipartFile file) throws IOException{
        DBObject metaData=new BasicDBObject();
        metaData.put("userId", userId);
        metaData.put("originalFileName", file.getOriginalFilename());

        ObjectId fileId=gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType(),
                metaData
        );

        Image image=Image.builder()
                .userId(userId)
                .originalFileName(file.getOriginalFilename())
                .fileIdGridFs(fileId.toHexString())
                .uploadDate(LocalDate.now())
                .build();

        return imageRepository.save(image);
    }

    public byte[] downloadImage(String fileId) throws IOException{
        var file=gridFsTemplate.findOne(
                new org.springframework.data.mongodb.core.query.Query(
                        org.springframework.data.mongodb.core.query.Criteria.where("_id").is(new ObjectId(fileId))
                )
        );
        if (file==null) throw new IOException("파일을 찾을 수 없습니다.");

        try(InputStream inputStream=gridFsOperations.getResource(file).getInputStream()){
            return inputStream.readAllBytes();
        }
    }

    public List<ImageResponseDto> getImagesByUser(String userId){
        return imageRepository.findByuserId(userId).stream()
                .map(image -> new ImageResponseDto(image.getOriginalFileName(), image.getFileIdGridFs(), image.getUploadDate()))
                .collect(Collectors.toList());
    }
}
