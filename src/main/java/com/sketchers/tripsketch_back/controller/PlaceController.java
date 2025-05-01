package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.dto.AccommodationReqDto;
import com.sketchers.tripsketch_back.dto.PlaceRespDto;
import com.sketchers.tripsketch_back.dto.StoredPlaceReqDto;
import com.sketchers.tripsketch_back.dto.TripReqDto;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import com.sketchers.tripsketch_back.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/api/places")
    public ResponseEntity<?> getPlacesByTextSearch(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String pagetoken
    ) {
        return ResponseEntity.ok(placeService.getPlacesByTextSearch(type, keyword, pagetoken));
    }

}
