package com.sketchers.tripsketch_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final RestTemplate restTemplate;

    @Value("${google.places.api.key}")
    private String apiKey;

    public byte[] fetchPlacePhoto(String photoReference) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/photo")
                .queryParam("maxwidth", 600)
                .queryParam("photo_reference", photoReference)
                .queryParam("key", apiKey)
                .toUriString();

        return restTemplate.getForObject(url, byte[].class);
    }
}
