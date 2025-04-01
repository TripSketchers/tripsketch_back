package com.sketchers.tripsketch_back.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {
    public int deleteUser(int userId);
    public int updateEnabledToEmail(String email);
}
