package com.sketchers.tripsketch_back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Album {
    private int albumId;
    private String date;
    private String placeName;
    private String startTime;
    private String photoUrl;
    private boolean tripScheduleMissingFlag;
}
