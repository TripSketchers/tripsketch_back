package com.sketchers.tripsketch_back.dto;

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
    private String startDate;   // trip 시작 날짜
    private List<AlbumRespDto> albums;
}
