package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.repository.TripMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final RestTemplate restTemplate;
    private final TripMapper tripMapper;

    @Value("${google.places.api.key}")
    private String apiKey;

    public byte[] fetchPlacePhoto(String photoName) {
        String url = buildPhotoMediaUrl(photoName);

        try {
            return restTemplate.getForObject(url, byte[].class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                System.out.println("⚠ 유효하지 않은 photo name → fresh name으로 재시도");

                String placeId = extractPlaceIdFromName(photoName);
                String newPhotoName = fetchFreshPhotoNameFromDetails(placeId);
                if (newPhotoName != null) {
                    tripMapper.updatePhotoReference(placeId, newPhotoName);
                    System.out.println("✅ DB 업데이트 완료: " + newPhotoName);

                    String newUrl = buildPhotoMediaUrl(newPhotoName);
                    return restTemplate.getForObject(newUrl, byte[].class);
                }
            }
            throw e;
        }
    }

    private String buildPhotoMediaUrl(String photoName) {
        return UriComponentsBuilder.fromHttpUrl("https://places.googleapis.com/v1/" + photoName + "/media")
                .queryParam("maxWidthPx", 600)
                .queryParam("key", apiKey)
                .toUriString();
    }

    private String fetchFreshPhotoNameFromDetails(String placeId) {
        if (placeId == null) return null;

        String url = UriComponentsBuilder.fromHttpUrl("https://places.googleapis.com/v1/places/" + placeId)
                .queryParam("fields", "photos")
                .queryParam("key", apiKey)
                .toUriString();

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null || !response.containsKey("photos")) return null;

            List<Map<String, Object>> photos = (List<Map<String, Object>>) response.get("photos");
            if (photos != null && !photos.isEmpty()) {
                return (String) photos.get(0).get("name");
            }
        } catch (Exception e) {
            System.out.println("❌ Place Details API 호출 실패: " + e.getMessage());
        }

        return null;
    }

    private String extractPlaceIdFromName(String photoName) {
        Pattern pattern = Pattern.compile("places/([^/]+)/photos");
        Matcher matcher = pattern.matcher(photoName);
        return matcher.find() ? matcher.group(1) : null;
    }
}
