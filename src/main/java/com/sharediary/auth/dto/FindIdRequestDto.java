package com.sharediary.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindIdRequestDto {
    private String email;
}
