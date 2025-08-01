package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.PhotoRespDto;
import com.sketchers.tripsketch_back.dto.TripDestinationRespDto;
import com.sketchers.tripsketch_back.dto.trip.TripDto;
import com.sketchers.tripsketch_back.entity.Photo;
import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.repository.MainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {
    private final MainMapper mainMapper;

    public TripDestinationRespDto getTripDestinations(String searchKeyword) {
        List<String> keywords = null;
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            keywords = Arrays.stream(searchKeyword.trim().split("\\s+"))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
        return TripDestinationRespDto.builder()
                .tripDestinations(mainMapper.getTripDestinations(keywords))
                .build();
    }

    public TripDto getUpcomingTrip(int userId) {
        Trip trip = mainMapper.getUpcomingTrip(userId);
        if (trip == null) {
            return null; // 또는 Optional.empty(), 또는 적절한 응답 처리
        }
        return trip.toTripDto();
    }

    public List<PhotoRespDto> getRecentAlbums(int userId) {
        List<Photo> photos = mainMapper.getRecentAlbums(userId);
        return photos.stream()
                .map(Photo::toPhotoDto) // Photo 객체를 PhotoRespDto로 변환
                .collect(Collectors.toList()); // List<PhotoRespDto>로 반환
    }

    public TripDestinationRespDto getPopularDomesticTrip() {
        return TripDestinationRespDto.builder()
                .tripDestinations(mainMapper.getPopularDomesticTrip())
                .build();
    }

    public TripDestinationRespDto getPopularOverseasTrip() {
        return TripDestinationRespDto.builder()
                .tripDestinations(mainMapper.getPopularOverseasTrip())
                .build();
    }

    public Map<String, TripDestinationRespDto> getPopularTrips() {
        Map<String, TripDestinationRespDto> result = new HashMap<>();
        result.put("domestic", getPopularDomesticTrip());
        result.put("overseas", getPopularOverseasTrip());
        return result;
    }
}
