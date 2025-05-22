package com.sketchers.tripsketch_back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoRespDto {
    private int tripId;
    private int albumId;
    private int photoId;
    private String photoUrl;
    private String memo;
}
