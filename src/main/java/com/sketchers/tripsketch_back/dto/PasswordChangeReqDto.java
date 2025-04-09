package com.sketchers.tripsketch_back.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PasswordChangeReqDto {

    @NotBlank(message = "새 비밀번호를 입력하세요.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{4,20}$",
        message = "비밀번호는 4~20자, 소문자/대문자/숫자를 모두 포함해야 합니다."
    )
    private String newPassword;
}
