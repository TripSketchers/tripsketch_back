package com.sketchers.tripsketch_back.entity;

import com.sketchers.tripsketch_back.dto.Mypage.TripShareDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripShare {
    private int shareId;
    private int tripId;
    private int sharedByUserId;
    private int sharedWithUserId;
    private String email;
    private Timestamp sharedAt;
    private String status;

    public TripShareDto toTripShareDto() {
        return TripShareDto.builder()
                .shareId(shareId)
                .tripId(tripId)
                .sharedByUserId(sharedByUserId)
                .sharedWithUserId(sharedWithUserId)
                .email(email)
                .sharedAt(sharedAt)
                .status(status)
                .build();
    }
}
