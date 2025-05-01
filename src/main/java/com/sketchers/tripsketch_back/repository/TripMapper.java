package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.entity.Trip;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TripMapper {
    public int insertTrip(Trip trip);
}
