package com.sketchers.tripsketch_back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketchers.tripsketch_back.dto.PlaceRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${google.places.api.key}")
    private String apiKey;

    public PlaceRespDto.textSearch getPlacesByTextSearch(String type, String keyword, String pagetoken) {
        String url = "https://places.googleapis.com/v1/places:searchText";

        try {
            // ✅ textQuery 조합
            StringBuilder textQuery = new StringBuilder();
            if (keyword != null && !keyword.isBlank()) {
                textQuery.append(keyword);
            }
            if (type != null && !type.isBlank()) {
                if (textQuery.length() > 0) {
                    textQuery.append(" ");
                }
                textQuery.append(type);
            }

            Map<String, Object> body = new HashMap<>();
            body.put("textQuery", textQuery.toString());
            body.put("pageSize", 10);
            body.put("languageCode", "ko");
            body.put("locationRestriction", Map.of(
                "rectangle", Map.of(
                    "low", Map.of("latitude", 35.05, "longitude", 128.94),
                    "high", Map.of("latitude", 35.32, "longitude", 129.28)
                )
            ));

            if (pagetoken != null && !pagetoken.isBlank()) {
                body.put("pageToken", pagetoken);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Goog-Api-Key", apiKey);
            headers.set("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress,places.location,places.photos,places.rating,places.types,nextPageToken");

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

            ResponseEntity<PlaceRespDto.textSearch> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                PlaceRespDto.textSearch.class
            );

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
