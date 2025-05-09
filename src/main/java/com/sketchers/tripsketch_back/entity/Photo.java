package com.sketchers.tripsketch_back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Photo {
    private int photoId;
    private String photoUrl;
    private String memo;
}