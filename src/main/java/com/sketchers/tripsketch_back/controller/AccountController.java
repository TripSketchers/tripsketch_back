package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.dto.PrincipalRespDto;
import com.sketchers.tripsketch_back.entity.User;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import com.sketchers.tripsketch_back.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/api/account/principal")
    public ResponseEntity<?> getPrincipal() {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principalUser.getUser();
        PrincipalRespDto principalRespDto = user.toPrincipalDto();
        return ResponseEntity.ok(principalRespDto);
    }

    @DeleteMapping("/api/account/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable int userId) {
        return ResponseEntity.ok(accountService.deleteUser(userId));
    }

    // 이메일 인증하기
    @PostMapping("/api/account/auth/email")
    public ResponseEntity<?> sendAuthenticationMail() {
        return ResponseEntity.ok(accountService.sendAuthMail());
    }

    // 인증된 이메일 가져오기
    @GetMapping("/api/account/auth/email")
    public ResponseEntity<?> authenticateMail (String token) {
        //주소의 토큰을 받고 유효한지 확인
        return ResponseEntity.ok(accountService.authenticateMail(token) ? "인증 완료" : "인증 실패");
    }

    @PostMapping("/api/account/check-password")
    public ResponseEntity<?> checkPassword(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(accountService.checkPassword(request.get("password")));
    }
}
