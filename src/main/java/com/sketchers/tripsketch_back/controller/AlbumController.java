package com.sketchers.tripsketch_back.controller;

import com.sketchers.tripsketch_back.security.PrincipalUser;
import com.sketchers.tripsketch_back.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

//    // 여행 앨범의 사진 목록 조회(폴더 목록 조회)
//    @GetMapping("/api/trips/{tripId}/album/folders")
//    public ResponseEntity<?> getAlbumFolders(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId) {
//        int userId = principalUser.getUser().getUserId();
//        return ResponseEntity.ok(albumService.getAlbumFolders(userId, tripId));
//    }
//
//    // 여행 앨범의 사진 목록 조회(폴더별 사진 조회)
//    @GetMapping("/api/trips/{tripId}/albums/{albumId}")
//    public ResponseEntity<?> getPhotosByFolder(@AuthenticationPrincipal PrincipalUser principalUser, @PathVariable int tripId,  @PathVariable int albumId) {
//        int userId = principalUser.getUser().getUserId();
//        return ResponseEntity.ok(albumService.getPhotosByFolder(userId, tripId, albumId));
//    }

}
