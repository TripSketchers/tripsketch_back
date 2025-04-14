package com.sketchers.tripsketch_back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Trip {
    private int tripId;
    private int userId;
    private String title;
    private Date startDate;
    private Date endDate;
    private String tripDestinationKoName;
}
