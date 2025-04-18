package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.dto.PlaceRespDto;
import com.sketchers.tripsketch_back.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/api/places")
    public ResponseEntity<?> getAllPlaces(@RequestParam String destination, @RequestParam String type, @RequestParam String pagetoken) {
        return ResponseEntity.ok(placeService.getPlacesByType(destination, type, pagetoken));
    }
}
