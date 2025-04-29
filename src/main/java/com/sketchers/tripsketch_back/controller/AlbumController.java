package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.dto.AlbumUploadReqDto;
import com.sketchers.tripsketch_back.security.PrincipalUser;
import com.sketchers.tripsketch_back.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

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

    @PutMapping("/api/trips/{tripId}/album/{photoId}")
    public ResponseEntity<?> editPhotoMemo(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId,  @PathVariable int photoId, @RequestBody String memo){
        return ResponseEntity.ok(albumService.editPhotoMemo(photoId, memo));
    }

    @DeleteMapping("/api/trips/{tripId}/albums/{albumId}")
    public ResponseEntity<?> deleteAlbum(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId, @PathVariable int albumId){
        return ResponseEntity.ok(albumService.deleteAlbum(tripId, albumId));
    }

    @DeleteMapping("/api/trips/{tripId}/album/photos/{photoId}")
    public ResponseEntity<?> deletePhoto(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId, @PathVariable int photoId){
        return ResponseEntity.ok(albumService.deletePhoto(tripId, photoId));
    }

}
