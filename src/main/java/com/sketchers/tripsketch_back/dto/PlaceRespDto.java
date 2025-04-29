package com.sketchers.tripsketch_back.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
}
