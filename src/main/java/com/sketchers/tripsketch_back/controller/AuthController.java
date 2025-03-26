package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.aop.annotation.ValidAop;
import com.sketchers.tripsketch_back.dto.SigninReqDto;
import com.sketchers.tripsketch_back.dto.SignupReqDto;
import com.sketchers.tripsketch_back.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ValidAop
    @PostMapping("/api/auth/signup")
    public ResponseEntity<?> signup (
            @Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult){
        return ResponseEntity.ok(authService.signup(signupReqDto));
    }

    @ValidAop
    @PostMapping("/api/auth/signin")
    public ResponseEntity<?> signin (
            @Valid @RequestBody SigninReqDto signinReqDto, BindingResult bindingResult) {
        return ResponseEntity.ok(authService.signin(signinReqDto));
    }
}
