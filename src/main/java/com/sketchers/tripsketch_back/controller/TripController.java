package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.dto.trip.StoredPlaceUpdateReqDto;
import com.sketchers.tripsketch_back.dto.trip.TripCreateReqDto;
import com.sketchers.tripsketch_back.service.TripService;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/api/trip")
    public ResponseEntity<?> createTrip(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @Valid @RequestBody TripCreateReqDto tripCreateReqDto
    ) {
        tripCreateReqDto.getTrip().setUserId(principalUser.getUser().getUserId());
        return ResponseEntity.ok(tripService.insertTrip(tripCreateReqDto));
    }

    @PutMapping("/api/trip/{tripId}")
    public ResponseEntity<?> updateTrip(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @Valid @RequestBody TripCreateReqDto tripCreateReqDto
    ) {
        tripCreateReqDto.getTrip().setUserId(principalUser.getUser().getUserId());
        return ResponseEntity.ok(tripService.updateTrip(tripCreateReqDto));
    }

    @GetMapping("/api/trips/{tripId}")
    public ResponseEntity<?> getTrip(@PathVariable int tripId) {
        return ResponseEntity.ok(tripService.getTripInfo(tripId));
    }

    @PostMapping("/api/trips/{tripId}/places")
    public ResponseEntity<?> updateStoredPlaces(
            @PathVariable int tripId,
            @Valid @RequestBody StoredPlaceUpdateReqDto storedPlaceUpdateReqDto
    ) {
        return ResponseEntity.ok(tripService.updateStoredPlaces(tripId, storedPlaceUpdateReqDto));
    }

    @GetMapping("/api/trips/traveltime")
    public ResponseEntity<?> getTravelTime(
            @RequestParam double originLat,
            @RequestParam double originLng,
            @RequestParam double destLat,
            @RequestParam double destLng,
            @RequestParam String mode
    ) {
        return ResponseEntity.ok(tripService.getTravelTimeWithRoutesAPI(originLat, originLng, destLat, destLng, mode));
    }
}
