package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    public User findUserByOauth2Id(String oauth2Id);
    public User findUserByEmail(String email);
}
