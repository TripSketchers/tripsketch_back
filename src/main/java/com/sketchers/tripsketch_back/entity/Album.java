package com.sketchers.tripsketch_back.entity;

import com.sketchers.tripsketch_back.dto.AlbumRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class Album {
    private int albumId;
    private int tripId;
    private String date;
    private String place;
}
