package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.aop.annotation.ValidAop;
import com.sketchers.tripsketch_back.dto.Mypage.ShareTripReqDto;
import com.sketchers.tripsketch_back.dto.PasswordChangeReqDto;
import com.sketchers.tripsketch_back.dto.PrincipalRespDto;
import com.sketchers.tripsketch_back.entity.User;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import com.sketchers.tripsketch_back.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/api/account/principal")
    public ResponseEntity<?> getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증되지 않은 경우
        if (authentication == null || !authentication.isAuthenticated()
            || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        return ResponseEntity.ok(principalUser.getUser().toPrincipalDto());
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

    @PostMapping("/api/account/password/check")
    public ResponseEntity<?> checkPassword(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(accountService.checkPassword(request.get("password")));
    }

    @ValidAop
    @PutMapping("/api/account/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeReqDto passwordChangeReqDto) {
        return ResponseEntity.ok(accountService.updatePassword(passwordChangeReqDto));
    }

    @GetMapping("/api/account/trips")
    public ResponseEntity<?> getTrips() {
        return ResponseEntity.ok(accountService.getTrips());
    }

    @DeleteMapping("/api/account/trip/{tripId}")
    public ResponseEntity<?> deleteTrip(@PathVariable int tripId) {
        return ResponseEntity.ok(accountService.deleteTrip(tripId));
    }

    @PostMapping("/api/account/trips/{tripId}/share")
    public ResponseEntity<?> shareTrip(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId, @Valid @RequestBody ShareTripReqDto shareTripReqDto) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(accountService.shareTrip(userId, tripId, shareTripReqDto));
    }

    @GetMapping("/api/account/trips/{tripId}/share")
    public ResponseEntity<?> getSharedUsers(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(accountService.getSharedUsers(userId, tripId));
    }

    @DeleteMapping("/api/account/trips/{tripId}/shares/{shareId}")
    public ResponseEntity<?> cancelShare(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId, @PathVariable int shareId) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(accountService.cancelShare(userId, tripId, shareId));
    }

    @GetMapping("/api/account/invitations")
    public ResponseEntity<?> getReceivedInvitations(@AuthenticationPrincipal PrincipalUser principalUser) {
        String email = principalUser.getUser().getEmail();
        return ResponseEntity.ok(accountService.getReceivedInvitations(email));
    }

    // 공유 초대 수락 -> share_tb의 status="accept"로 변경
    @PutMapping("/api/account/invitations/{shareId}/accept")
    public ResponseEntity<?> acceptTripInvitation(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int shareId) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(accountService.acceptTripInvitation(shareId));
    }

    // 공유 초대 거절 -> share_tb에서 삭제 처리
    @DeleteMapping("/api/account/invitations/{shareId}")
    public ResponseEntity<?> declineTripInvitation(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int shareId) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(accountService.declineTripInvitation(shareId));
    }
}
