package com.sketchers.tripsketch_back.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class AlbumCreateReqDto {
    private int albumId;  // INSERT 후 keyProperty로 주입 받을 필드
    private int userId;
    private int tripId;
    private int tripScheduleId;
    private String date;
    private String placeName;
    private String startTime;
}
