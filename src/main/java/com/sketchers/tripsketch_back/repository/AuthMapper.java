package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    public User findUserByEmail(String email);
    public Boolean checkDuplicate(String email);
    public int saveUser(User user);
    public int updateTripShareUserId(String email, int userId);
}
