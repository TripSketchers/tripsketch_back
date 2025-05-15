package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/api/places")
    public ResponseEntity<?> getPlacesByTextSearch(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String pagetoken,
            @RequestParam(required = false) List<Double> location
    ) {
        return ResponseEntity.ok(placeService.getPlacesByTextSearch(type, keyword, pagetoken, location));
    }

}
