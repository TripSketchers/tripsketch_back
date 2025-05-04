package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> duplicateException(DuplicateException duplicateException) {
        return ResponseEntity.badRequest().body(duplicateException.getErrorMap());
    }

    @ExceptionHandler(ValidException.class)
    public ResponseEntity<?> validException(ValidException validException) {
        return ResponseEntity.badRequest().body(validException.getErrorMap());
    }

    @ExceptionHandler(SigninException.class)
    public ResponseEntity<?> signinException(SigninException signinException) {
        return ResponseEntity.badRequest().body(signinException.getErrorMap());
    }

    @ExceptionHandler(SendMailException.class)
    public ResponseEntity<?> sendMailException(SendMailException sendMailException) {
        Map<String, String> message = new HashMap<>();
        message.put("sendFail", "이메일 전송이 실패했습니다.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    @ExceptionHandler(TripInsertException.class)
    public ResponseEntity<?> handleTripInsertException(TripInsertException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
