package com.sketchers.tripsketch_back.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class PlaceRespDto {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class address {
        private List<addressInfo> results;
        private String next_page_token;
        private String status;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class addressInfo {
        private String place_id;
        private String formatted_address;
        private geometryInfo geometry;
        private String name;
        private Double rating;
        private List<photoInfo> photos;
        private String photoUrl;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class geometryInfo {
        private locationInfo location;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class locationInfo {
        private double lat;
        private double lng;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class photoInfo {
        private String photo_reference;
        private int width;
        private int height;
    }
}
