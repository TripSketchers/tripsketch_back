package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SigninReqDto {
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
}
