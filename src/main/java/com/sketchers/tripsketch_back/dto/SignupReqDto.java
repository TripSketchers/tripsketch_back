package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignupReqDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public User toUser() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
