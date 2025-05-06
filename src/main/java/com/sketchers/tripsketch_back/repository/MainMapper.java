package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.entity.TripDestination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MainMapper {
    public List<TripDestination> getTripDestinations(@Param("keywords") List<String> keywords);
}
