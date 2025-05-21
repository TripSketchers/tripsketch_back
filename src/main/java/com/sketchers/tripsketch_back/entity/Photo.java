package com.sketchers.tripsketch_back.entity;

import com.sketchers.tripsketch_back.dto.PhotoRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Photo {
    private int tripId;
    private int albumId;
    private int photoId;
    private String photoUrl;
    private String memo;

    public PhotoRespDto toPhotoDto() {
        return PhotoRespDto.builder()
                .tripId(tripId)
                .albumId(albumId)
                .photoId(photoId)
                .photoUrl(photoUrl)
                .memo(memo)
                .build();
    }
}