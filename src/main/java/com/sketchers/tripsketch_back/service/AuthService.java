package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.SignupReqDto;
import com.sketchers.tripsketch_back.entity.User;
import com.sketchers.tripsketch_back.exception.DuplicateException;
import com.sketchers.tripsketch_back.repository.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;

    public boolean signup(SignupReqDto signupReqDto) {
        User user = signupReqDto.toUser();
        if(authMapper.checkDuplicate(user.getEmail())) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("email", "이미 사용 중인 이메일입니다.");
            throw new DuplicateException(errorMap);
        }
        return authMapper.saveUser(user) > 0;
    }
}
