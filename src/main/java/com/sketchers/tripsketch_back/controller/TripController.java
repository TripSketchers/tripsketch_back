package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.dto.AccommodationReqDto;
import com.sketchers.tripsketch_back.dto.StoredPlaceReqDto;
import com.sketchers.tripsketch_back.dto.TripReqDto;
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
            TripReqDto tripReqDto,
            StoredPlaceReqDto storedPlaceReqDto,
            AccommodationReqDto accommodationReqDto
    ) {
        tripReqDto.setUserId(principalUser.getUser().getUserId());
        return ResponseEntity.ok(tripService.insertTrip(tripReqDto, storedPlaceReqDto, accommodationReqDto));
    }
}
