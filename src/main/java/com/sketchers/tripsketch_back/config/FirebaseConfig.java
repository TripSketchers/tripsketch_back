package com.sketchers.tripsketch_back.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Component
public class FirebaseConfig {

    @Value("${firebase.bucket-name}")
    private String bucketName;

    @PostConstruct
    public void initialize() throws Exception {
        // resources/firebase/firebase-service-account.json 경로에서 파일을 읽음
        ClassPathResource resource = new ClassPathResource("firebase/firebase-service-account.json");

        try (InputStream serviceAccount = resource.getInputStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            System.out.println("✅ Firebase 프로젝트 ID: " + credentials.getQuotaProjectId());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setStorageBucket(bucketName)
                    .build();

            List<FirebaseApp> apps = FirebaseApp.getApps();

            if (!apps.isEmpty()) {
                for (FirebaseApp app : apps) {
                    System.out.println("❗ 기존 FirebaseApp 삭제: " + app.getName());
                    app.delete();
                }
            }

            FirebaseApp.initializeApp(options);
            System.out.println("✅ FirebaseApp 재초기화 완료");
        }
    }
}
