package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {
    public int deleteUser(int userId);
    public int updateEnabledToEmail(String email);
    public int updatePassword(User user);
    public List<Trip> findTripsByUserId(int userId);
}
