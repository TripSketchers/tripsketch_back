package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignupReqDto {

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{4,20}$",
        message = "비밀번호는 4~20자, 소문자/대문자/숫자를 모두 포함해야 합니다."
    )
    private String password;

    public User toUser() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
