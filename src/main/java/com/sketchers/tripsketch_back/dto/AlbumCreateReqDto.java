package com.sketchers.tripsketch_back.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AlbumCreateReqDto {
    private Integer albumId;  // INSERT 후 keyProperty로 주입 받을 필드
    private Integer userId;
    private Integer tripId;
    private Integer tripScheduleId;
}
