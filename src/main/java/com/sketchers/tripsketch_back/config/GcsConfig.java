package com.sketchers.tripsketch_back.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class GcsConfig {

    @Bean
    public Storage gcsStorage() throws IOException {
        String firebaseBase64 = System.getenv("FIREBASE_CONFIG");
        if (firebaseBase64 == null || firebaseBase64.isEmpty()) {
            throw new IllegalStateException("FIREBASE_CONFIG 환경 변수가 설정되지 않았습니다.");
        }

        byte[] decodedBytes = Base64.getDecoder().decode(firebaseBase64);
        ByteArrayInputStream credentialsStream = new ByteArrayInputStream(decodedBytes);

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
