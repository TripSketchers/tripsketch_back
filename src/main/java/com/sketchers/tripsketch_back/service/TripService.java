package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.AccommodationReqDto;
import com.sketchers.tripsketch_back.dto.StoredPlaceReqDto;
import com.sketchers.tripsketch_back.dto.TripReqDto;
import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.repository.TripMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripMapper tripMapper;

    public boolean insertTrip(TripReqDto tripReqDto, StoredPlaceReqDto storedPlaceReqDto, AccommodationReqDto accommodationReqDto) {
        Trip trip = tripReqDto.toTrip();
        tripMapper.insertTrip(trip);

//        if (tripMapper.findByGooglePlaceId(place.getGooglePlaceId()) == 0) {
//            placeRepository.save(place);  // 신규 장소만 저장
//        }

        return true;
    }
}
