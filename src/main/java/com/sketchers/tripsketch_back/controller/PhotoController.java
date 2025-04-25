package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping("/photo")
    public ResponseEntity<byte[]> getPhoto(@RequestParam("ref") String photoReference) {
        byte[] imageBytes = photoService.fetchPlacePhoto(photoReference);

        if (imageBytes == null || imageBytes.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
