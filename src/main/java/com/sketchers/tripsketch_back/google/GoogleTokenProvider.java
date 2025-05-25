package com.sketchers.tripsketch_back.google;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class GoogleTokenProvider {

    private final GoogleCredentials credentials;

    public GoogleTokenProvider() {
        try {
            InputStream serviceAccountStream =
                    new ClassPathResource("tripsketch-454909-9189a505ba8e.json").getInputStream();

            this.credentials = GoogleCredentials.fromStream(serviceAccountStream)
                    .createScoped(List.of(
                            "https://www.googleapis.com/auth/cloud-platform"
                    ));

            credentials.refreshIfExpired(); // 최초 토큰 생성

            System.out.println("accessToken: " + credentials.getAccessToken().getTokenValue());
            // ✅ 반드시 refresh 후 getAccessToken
        } catch (IOException e) {
            throw new RuntimeException("❌ Google Service Account 초기화 실패", e);
        }
    }

    public String getAccessToken() {
        try {
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException("❌ Access Token 발급 실패", e);
        }
    }
}
