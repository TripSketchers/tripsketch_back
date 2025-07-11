package com.sketchers.tripsketch_back.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AlbumUploadReqDto {
    private int tripScheduleId;
    private String date;
    private String placeName;
    private String startTime;
    private List<PhotoDto> photos;

    @Data
    public static class PhotoDto {
        private String photoUrl;
        private String memo;
    }
}
