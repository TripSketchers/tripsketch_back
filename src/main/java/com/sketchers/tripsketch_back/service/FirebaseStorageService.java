package com.sketchers.tripsketch_back.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class FirebaseStorageService {
    @Value("${firebase.bucket-name}")
    private String bucketName;
    private final Storage storage;

    public boolean deletePhotoFromFirebase(String photoUrl) {
        try {
            String filePath = extractFilePathFromUrl(photoUrl);

            // Firebase SDK에서 StorageClient 사용
            StorageClient storageClient = StorageClient.getInstance();
            Blob blob = storageClient.bucket(bucketName).get(filePath);
            if (blob != null) {
                boolean deleted = blob.delete();
                if (!deleted) {
                    System.err.println("❗ Firebase에서 파일 삭제 실패 (권한 부족 등): " + filePath);
                }
                return deleted;
            } else {
                System.err.println("❗ Firebase에 해당 파일이 존재하지 않음: " + filePath);
                return false;
            }
        } catch (Exception e) {
            System.err.println("🔥 Firebase 사진 삭제 실패: " + e.getMessage());
            return false;
        }
}
    private String extractFilePathFromUrl(String photoUrl) {    //URL에서 Firebase 내부 저장 경로를 추출
        // 예: https://firebasestorage.googleapis.com/v0/b/{bucket}/o/tripsketch%2Ftrip-9%2Falbum-4%2F114.jpg?alt=media... 형식에서 경로만 추출
        try {
                String[] parts = photoUrl.split("/o/");
                if (parts.length < 2) {
                    throw new IllegalArgumentException("잘못된 Firebase URL 형식입니다.");
                }
                String encodedPath = parts[1].split("\\?")[0]; // 경로만 추출
                return java.net.URLDecoder.decode(encodedPath, java.nio.charset.StandardCharsets.UTF_8.name());
            } catch (Exception e) {
                throw new RuntimeException("Firebase 파일 경로 추출 중 오류 발생", e);
            }
    }

    public boolean deleteFolderFromFirebase(String folderPath) {
        try {
            Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(folderPath));
            ExecutorService executor = Executors.newFixedThreadPool(10); // 병렬 삭제용

            List<Blob> blobList = StreamSupport.stream(blobs.iterateAll().spliterator(), false)
                                               .collect(Collectors.toList());

            if (blobList.isEmpty()) {
                System.out.println("📁 삭제할 파일이 없습니다. 경로 확인: " + folderPath);
                return true;
            }

            for (Blob blob : blobList) {
                System.out.println("🔍 삭제 시도: " + blob.getName());

                executor.execute(() -> {
                    boolean deleted = blob.delete();
                    if (deleted) {
                        System.out.println("✅ 삭제 성공: " + blob.getName());
                    } else {
                        System.err.println("❌ 삭제 실패: " + blob.getName());
                    }
                });
            }

            executor.shutdown();
            while (!executor.isTerminated()) {
                Thread.sleep(100); // 삭제 완료까지 대기
            }

            return true;
        } catch (Exception e) {
            System.err.println("Firebase 삭제 중 오류: " + e.getMessage());
            return false;
        }
    }
}
