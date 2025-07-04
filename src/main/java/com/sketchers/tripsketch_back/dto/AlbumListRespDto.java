package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.Album;
import com.sketchers.tripsketch_back.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumListRespDto { //Album 상위 응답 DTO   //중복 최소화
    private Trip trip;
    private List<Album> albums;
}
