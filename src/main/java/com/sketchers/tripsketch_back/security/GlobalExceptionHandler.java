package com.sketchers.tripsketch_back.security;

import com.sketchers.tripsketch_back.exception.FirebaseTokenException;
import com.sketchers.tripsketch_back.exception.SigninException;
import io.netty.util.Signal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FirebaseTokenException.class)
    public ResponseEntity<?> handleFirebaseTokenException(FirebaseTokenException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(SigninException.class)
    public ResponseEntity<?> handleSigninException(SigninException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }
}
