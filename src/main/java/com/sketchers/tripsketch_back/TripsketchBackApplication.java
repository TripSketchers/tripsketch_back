package com.sketchers.tripsketch_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@SpringBootApplication
public class TripsketchBackApplication {

	public static void main(String[] args) throws IOException {
		// 🔐 Firebase JSON 환경변수 디코딩
		String firebaseBase64 = System.getenv("FIREBASE_CONFIG");
		if (firebaseBase64 != null && !firebaseBase64.isEmpty()) {
			byte[] decoded = Base64.getDecoder().decode(firebaseBase64);
			Path tempFile = Files.createTempFile("firebase", ".json");
			Files.write(tempFile, decoded);

			// 🔧 Spring Boot에서 사용할 수 있도록 시스템 속성 등록
			System.setProperty("firebase.config.path", tempFile.toAbsolutePath().toString());
		}
		SpringApplication.run(TripsketchBackApplication.class, args);
	}

}
