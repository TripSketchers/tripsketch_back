package com.sketchers.tripsketch_back.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.sketchers.tripsketch_back.exception.FirebaseTokenException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseService {
    public String createFirebaseTokenWithClaims(String uid) {
        try {
            return FirebaseAuth.getInstance().createCustomToken(uid);
        } catch (FirebaseAuthException e) {
            throw new FirebaseTokenException("Firebase 토큰 생성 실패", e);
        }
    }
}
