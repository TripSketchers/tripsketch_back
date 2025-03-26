package com.sketchers.tripsketch_back.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class SigninException extends RuntimeException {
    private Map<String, String> errorMap;

    public SigninException(Map<String, String> errorMap) {
        super("로그인 오류");
        this.errorMap = errorMap;
    }
}
