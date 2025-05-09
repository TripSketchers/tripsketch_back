package com.sketchers.tripsketch_back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accommodation {
    private int accommodationId;
    private int tripId;
    private int placeId;
    private Date checkInDate;
    private Date checkOutDate;
}
