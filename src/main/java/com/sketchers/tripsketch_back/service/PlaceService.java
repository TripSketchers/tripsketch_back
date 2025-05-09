package com.sketchers.tripsketch_back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketchers.tripsketch_back.dto.PlaceRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
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
            // ✅ 검색 키워드만 textQuery에 입력
            Map<String, Object> body = new HashMap<>();
            body.put("textQuery", keyword);
            body.put("pageSize", 10);
            body.put("languageCode", "ko");

            // ✅ type이 있으면 includedType으로 필터링
            if (type != null && !type.isBlank()) {
                body.put("includedType", type);
            }

            // ✅ 지역 제한 설정 (부산 인근)
            body.put("locationRestriction", Map.of(
                    "rectangle", Map.of(
                            "low", Map.of("latitude", 35.05, "longitude", 128.94),
                            "high", Map.of("latitude", 35.32, "longitude", 129.28)
                    )
            ));

            // ✅ 다음 페이지 토큰이 있으면 추가
            if (pagetoken != null && !pagetoken.isBlank()) {
                body.put("pageToken", pagetoken);
            }

            // ✅ HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Goog-Api-Key", apiKey);
            headers.set("X-Goog-FieldMask",
                    "places.id,places.displayName,places.formattedAddress," +
                            "places.location,places.photos,places.rating,places.types,nextPageToken");

            // ✅ HTTP 요청 전송
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

            ResponseEntity<PlaceRespDto.textSearch> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    PlaceRespDto.textSearch.class
            );

            PlaceRespDto.textSearch result = response.getBody();

            // ✅ category 주입
            if (result != null && result.getPlaces() != null) {
                result.getPlaces().forEach(place -> {
                    String category = mapTypesToCategory(place.getTypes());
                    place.setCategory(category);
                });
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String mapTypesToCategory(List<String> types) {
        if (types == null || types.isEmpty()) return "기타";
        if (types.contains("lodging")) return "숙소";
        if (types.contains("cafe")) return "카페";
        if (types.contains("restaurant")) return "맛집";
        if (types.contains("tourist_attraction")) return "명소";
        return "기타";
    }
}
