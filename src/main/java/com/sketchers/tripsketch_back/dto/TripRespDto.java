package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.dto.Mypage.ShareTripRespDto;
import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.entity.TripShare;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripRespDto {
    private Trip tripList;
    private int shareId;
    private String sharedByUserEmail;
    private String status;
}