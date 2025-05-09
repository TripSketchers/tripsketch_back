package com.sketchers.tripsketch_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDeleteReqDto {
    private int albumId;
    private int photoId;
}
