package com.sketchers.tripsketch_back.dto.Mypage;

import com.sketchers.tripsketch_back.entity.TripShare;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripShareDto {
    private int shareId;
    private int tripId;
    private int sharedByUserId;
    private int sharedWithUserId;
    private String email;
    private Timestamp sharedAt;
    private boolean status;

    public TripShare toTripShare() {
        return TripShare.builder()
                .tripId(tripId)
                .sharedByUserId(sharedByUserId)
                .sharedWithUserId(sharedWithUserId)
                .email(email)
                .sharedAt(sharedAt)
                .status(status)
                .build();
    }
}
