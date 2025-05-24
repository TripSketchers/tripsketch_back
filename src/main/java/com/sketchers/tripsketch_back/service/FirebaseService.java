package com.sketchers.tripsketch_back.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.sketchers.tripsketch_back.exception.FirebaseTokenException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseService {
    public String createFirebaseTokenWithClaims(String uid, boolean isAdmin) {
        setClaims(uid, isAdmin);  // 권한 클레임 세팅

        try {
            return FirebaseAuth.getInstance().createCustomToken(uid);
        } catch (FirebaseAuthException e) {
            throw new FirebaseTokenException("Firebase 토큰 생성 실패", e);
        }
    }

    public void setClaims(String uid, boolean isAdmin) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("admin", isAdmin);
            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
        } catch (FirebaseAuthException e) {
            throw new FirebaseTokenException("Firebase 클레임 설정 실패", e);
        }
    }
}
