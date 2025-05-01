package com.sketchers.tripsketch_back.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sketchers.tripsketch_back.dto.trip.create.PlaceInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class PlaceRespDto {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class textSearch {
        private List<textSearchInfo> places; // ✅ TextSearch 결과
        private String nextPageToken;         // ✅ 다음 페이지 토큰
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class textSearchInfo {
        private String id;                     // 장소 ID (places/xxxx)
        private displayName displayName;       // 장소 이름
        private String formattedAddress;       // 주소
        private locationInfo location;         // 위도, 경도
        private List<photoInfo> photos;         // 사진 정보
        private List<String> types;             // 타입 (e.g., restaurant, cafe)
        private Double rating;                  // 평점
        private String category;                // 직접 추가
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class displayName {
        private String text;                    // 장소명 텍스트
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class locationInfo {
        private Double latitude;                // 위도
        private Double longitude;               // 경도
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class photoInfo {
        private String name;                    // 사진 이름 (photoReference 역할)
    }

    public PlaceInfoDto convertToPlaceInfoDto(PlaceRespDto.textSearchInfo info) {
        return PlaceInfoDto.builder()
                .googlePlaceId(info.getId())
                .name(info.getDisplayName().getText())
                .address(info.getFormattedAddress())
                .latitude(info.getLocation().getLatitude())
                .longitude(info.getLocation().getLongitude())
                .photoReference(info.getPhotos() != null && !info.getPhotos().isEmpty()
                        ? info.getPhotos().get(0).getName() : null)
                .rating(info.getRating())
                .category(info.getCategory() != null ? info.getCategory() : "기타")
                .build();
    }

}
