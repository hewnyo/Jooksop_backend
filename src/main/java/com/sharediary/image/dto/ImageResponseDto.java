package com.sharediary.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ImageResponseDto {
    private String originalFileName;
    private String fileIdInGridFs;
    private LocalDate uploadDate;
}
