package com.sketchers.tripsketch_back.dto;

import lombok.Data;

import java.util.List;

@Data
public class AlbumUploadReqDto {
    private int tripScheduleId;
    private List<PhotoDto> photos;

    @Data
    public static class PhotoDto {
        private String photoUrl;
        private String memo;
    }
}
