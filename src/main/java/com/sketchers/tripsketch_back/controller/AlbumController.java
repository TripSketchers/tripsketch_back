package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.dto.AlbumCreateReqDto;
import com.sketchers.tripsketch_back.dto.AlbumUploadReqDto;
import com.sketchers.tripsketch_back.dto.PhotoDeleteReqDto;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import com.sketchers.tripsketch_back.service.AlbumService;
import com.sketchers.tripsketch_back.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;
    private final FirebaseStorageService firebaseStorageService;

    // 여행 앨범 조회
    @GetMapping("/api/trips/{tripId}/albums")
    public ResponseEntity<?> getAlbums(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(albumService.getAlbums(userId, tripId));
    }

    // 여행 앨범의 사진 목록 조회
    @GetMapping("/api/trips/album/{albumId}/photos")
    public ResponseEntity<?> getPhotosByAlbumId(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int albumId) {
        return ResponseEntity.ok(albumService.getPhotosByAlbumId(albumId));
    }

    // 업로드 페이지 - 여행 일정 불러오기
    @GetMapping("/api/trips/{tripId}/schedules")
    public ResponseEntity<?> getTripSchedules(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId){
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(albumService.getTripSchedules(userId,tripId));
    }

    // 업로드 페이지 - 여행 앨범 생성 및 사진 업로드
    @PostMapping("/api/trips/{tripId}/album")
    public ResponseEntity<?> createTripAlbum(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId, @RequestBody AlbumUploadReqDto albumUploadReqDto){
        int userId = principalUser.getUser().getUserId();
        boolean result = albumService.createTripAlbum(userId, tripId, albumUploadReqDto);

        if (result) {
            return ResponseEntity.ok().body("사진 업로드 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사진 업로드 실패");
        }
    }

    // 사진 디테일 모달 - 특정 사진 정보 수정(메모)
    @PutMapping("/api/trips/{tripId}/album/{photoId}")
    public ResponseEntity<?> editPhotoMemo(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId,  @PathVariable int photoId, @RequestBody String memo){
        return ResponseEntity.ok(albumService.editPhotoMemo(photoId, memo));
    }

    // 개별 앨범 삭제
    @DeleteMapping("/api/trips/{tripId}/albums/{albumId}")
    public ResponseEntity<?> deleteAlbum(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId, @PathVariable int albumId){
        return ResponseEntity.ok(albumService.deleteAlbum(albumId));
    }

    // 개별 사진 삭제
    @DeleteMapping("/api/trips/{tripId}/album/photos/{photoId}")
    public ResponseEntity<?> deletePhoto(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId, @PathVariable int photoId){
        int userId= principalUser.getUser().getUserId();
        return ResponseEntity.ok(albumService.deletePhoto(userId, tripId, photoId));
    }

    // 선택한 사진 여러 개 삭제
    @DeleteMapping("/api/trips/{tripId}/album/photos")
    public ResponseEntity<?> deleteSelectedPhotos(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId, @RequestBody List<PhotoDeleteReqDto> checkedPhotos){
        int userId= principalUser.getUser().getUserId();
        return ResponseEntity.ok(albumService.deleteSelectedPhotos(userId, tripId, checkedPhotos));
    }

    @PutMapping("/api/trips/{tripId}/album")
    public ResponseEntity<?> editAlbumSchedule(@AuthenticationPrincipal PrincipalUser principalUser, @RequestBody AlbumCreateReqDto albumInfo) {
        int userId= principalUser.getUser().getUserId();
        return ResponseEntity.ok(albumService.editAlbumSchedule(userId, albumInfo));
    }

}
