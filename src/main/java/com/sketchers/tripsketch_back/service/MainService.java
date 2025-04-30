package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.TripDestinationRespDto;
import com.sketchers.tripsketch_back.repository.MainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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
        System.out.println("검색 키워드: " + keywords);
        return TripDestinationRespDto.builder()
                .tripDestinations(mainMapper.getTripDestinations(keywords))
                .build();
    }
}
