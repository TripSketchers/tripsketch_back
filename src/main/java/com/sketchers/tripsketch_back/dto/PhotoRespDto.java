package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.Photo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PhotoRespDto {
    private List<Photo> photos;
}
