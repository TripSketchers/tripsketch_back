package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.PlaceRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final RestTemplate restTemplate;
    @Value("${google.places.api.key}")
    private String apiKey;

    public PlaceRespDto.address getPlacesByType(String keyword, String type, String pagetoken) {
        String baseUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json";
        String query = keyword + " " + type;

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("query", query)
                .queryParam("key", apiKey)
                .queryParam("pagetoken", pagetoken)
                .queryParam("language", "ko").encode().build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        PlaceRespDto.address response = restTemplate
                .exchange(builder.toUri(), HttpMethod.GET, new HttpEntity<>(headers), PlaceRespDto.address.class)
                .getBody();

        if (response != null && response.getResults() != null) {
            for (PlaceRespDto.addressInfo place : response.getResults()) {
                if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
                    String ref = place.getPhotos().get(0).getPhoto_reference();
                    String photoUrl = "https://maps.googleapis.com/maps/api/place/photo"
                            + "?maxwidth=400"
                            + "&photo_reference=" + ref
                            + "&key=" + apiKey;
                    place.setPhotoUrl(photoUrl);
                }
            }
        }

        return response;
    }
}
