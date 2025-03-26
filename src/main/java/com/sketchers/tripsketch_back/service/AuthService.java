package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.SigninReqDto;
import com.sketchers.tripsketch_back.dto.SignupReqDto;
import com.sketchers.tripsketch_back.entity.User;
import com.sketchers.tripsketch_back.exception.DuplicateException;
import com.sketchers.tripsketch_back.exception.SigninException;
import com.sketchers.tripsketch_back.repository.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean signup(SignupReqDto signupReqDto) {
        User user = signupReqDto.toUser();
        if(authMapper.checkDuplicate(user.getEmail())) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("email", "이미 사용 중인 이메일입니다.");
            throw new DuplicateException(errorMap);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return authMapper.saveUser(user) > 0;
    }

    public boolean signin(SigninReqDto signinReqDto) {
        User user = authMapper.findUserByEmail(signinReqDto.getEmail());

        // 이메일이 없거나 비밀번호가 틀린 경우
        if (user == null || !passwordEncoder.matches(signinReqDto.getPassword(), user.getPassword())) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("signin", "이메일 또는 비밀번호가 일치하지 않습니다.");
            throw new SigninException(errorMap);
        }

        return true;
    }

}
