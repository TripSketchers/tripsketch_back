package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.dto.trip.create.StoredAccommodationReqDto;
import com.sketchers.tripsketch_back.dto.trip.create.StoredPlaceReqDto;
import com.sketchers.tripsketch_back.dto.trip.create.TripCreateReqDto;
import com.sketchers.tripsketch_back.dto.trip.create.TripReqDto;
import com.sketchers.tripsketch_back.service.TripService;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/api/trip")
    public ResponseEntity<?> createTrip(
            @AuthenticationPrincipal PrincipalUser principalUser,
            TripCreateReqDto tripCreateReqDto
    ) {
        tripCreateReqDto.getTrip().setUserId(principalUser.getUser().getUserId());
        return ResponseEntity.ok(tripService.insertTrip(tripCreateReqDto));
    }
}
