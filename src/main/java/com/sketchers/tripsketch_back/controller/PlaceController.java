package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.dto.PlaceRespDto;
import com.sketchers.tripsketch_back.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        PlaceRespDto.textSearch result = placeService.getPlacesByTextSearch(type, keyword, pagetoken);
        return ResponseEntity.ok(result);
    }
}
