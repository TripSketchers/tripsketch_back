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

    // 여행 앨범의 사진 목록 조회(전체 사진 조회)
    @GetMapping("/api/trips/{tripId}/album/photos")
    public ResponseEntity<?> getPhotosAll(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(albumService.getPhotosAll(userId, tripId));
    }

    // 여행 앨범의 사진 목록 조회(폴더 목록 조회)
    @GetMapping("/api/trips/{tripId}/album/folders")
    public ResponseEntity<?> getAlbumFolders(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(albumService.getAlbumFolders(userId, tripId));
    }

    // 여행 앨범의 사진 목록 조회(폴더별 사진 조회)
    @GetMapping("/api/trips/{tripId}/albums/{albumId}")
    public ResponseEntity<?> getPhotosByFolder(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId,  @PathVariable int albumId) {
        int userId = principalUser.getUser().getUserId();
        return ResponseEntity.ok(albumService.getPhotosByFolder(userId, tripId, albumId));
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
        return ResponseEntity.ok(albumService.editPhotoMemo(tripId, memo));
    }

}
