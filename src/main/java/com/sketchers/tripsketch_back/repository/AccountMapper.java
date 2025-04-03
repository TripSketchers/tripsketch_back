package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {
    public int deleteUser(int userId);
    public int updateEnabledToEmail(String email);
    public int updatePassword(User user);
}
