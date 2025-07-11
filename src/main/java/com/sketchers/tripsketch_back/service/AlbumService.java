package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.*;
import com.sketchers.tripsketch_back.entity.Album;
import com.sketchers.tripsketch_back.entity.Photo;
import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.repository.AlbumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumMapper albumMapper;
    private final FirebaseStorageService firebaseStorageService;

    public AlbumListRespDto getAlbums(int userId, int tripId) {
        List<Album> albums = albumMapper.getAlbums(userId, tripId);
        Trip trip = albumMapper.getTripInfo(userId, tripId);  // trip_tb에서 가져오기

        AlbumListRespDto albumListRespDto = AlbumListRespDto.builder()
                .trip(trip)
                .albums(albums)
                .build();
        return albumListRespDto;
    }

    public List<PhotoRespDto> getPhotosByAlbumId(int albumId) {
        List<Photo> photos = albumMapper.getPhotosByAlbumId(albumId);

        // Photo 객체를 PhotoRespDto로 변환
        return photos.stream()
                     .map(Photo::toPhotoDto) // Photo 객체를 PhotoRespDto로 변환
                     .collect(Collectors.toList()); // List<PhotoRespDto>로 반환
    }

    public TripScheduleRespDto getTripSchedules(int userId, int tripId) {
        return new TripScheduleRespDto(albumMapper.getTripSchedules(userId, tripId), albumMapper.getTripInfo(userId, tripId).toTripDto());
    }

    @Transactional //실패 시 롤백
    public boolean createTripAlbum(int userId, int tripId, AlbumUploadReqDto albumUploadReqDto) {
        try {
            //1. 해당 일정의 앨범이 존재하는지 검색
            String date = albumUploadReqDto.getDate();
            String placeName = albumUploadReqDto.getPlaceName();
            String startTime = albumUploadReqDto.getStartTime();

            int albumId = albumMapper.getAlbumId(userId, tripId, date, placeName, startTime);

            //2. 앨범이 없으면 새로 생성
            if (albumId == 0) {
                AlbumCreateReqDto request = AlbumCreateReqDto.builder()
                        .userId(userId)
                        .tripId(tripId)
                        .tripScheduleId(albumUploadReqDto.getTripScheduleId())
                        .date(albumUploadReqDto.getDate())
                        .placeName(albumUploadReqDto.getPlaceName())
                        .startTime(albumUploadReqDto.getStartTime())
                        .build();
                albumMapper.createTripAlbum(request);

                albumId = request.getAlbumId();  // 여기서 새로 생성된 albumId 가져옴

                if (albumId == 0) {
                    throw new RuntimeException("앨범 생성 실패");
                }
            }

            //3. 앨범에 사진 저장
            for (AlbumUploadReqDto.PhotoDto photoDto : albumUploadReqDto.getPhotos()) {
                int result = albumMapper.insertPhoto(albumId, photoDto.getPhotoUrl(), photoDto.getMemo());
                if (result <= 0) {
                    throw new RuntimeException("사진 저장 실패");
                }
            }
            return true;
        } catch (Exception e) {
            // 여기서 로그를 남기거나, custom 예외로 변환 가능
            throw new RuntimeException("앨범 업로드 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public boolean editPhotoMemo(int photoId, String memo) {
        return albumMapper.editPhotoMemo(photoId, memo);
    }

    public boolean deleteAlbum(int albumId) {
        return albumMapper.deleteAlbum(albumId);
    }

    @Transactional
    public String deletePhoto(int userId, int tripId, int photoId) {
        int ownerId = albumMapper.findOwner(tripId, photoId);
        if (ownerId != userId) {
            return "이 앨범을 삭제할 권한이 없습니다.";
        }

        String photoUrl = albumMapper.getPhoto(photoId).getPhotoUrl();
        boolean deletedFromFirebase = firebaseStorageService.deletePhotoFromFirebase(photoUrl);
        if (!deletedFromFirebase) {
            return "사진 삭제 실패 (Firebase)";
        }

        // 1. 앨범 ID 조회 (삭제 전 album_id 확보)
        int albumId = albumMapper.findAlbumId(photoId);

        // 2. 사진 삭제
        albumMapper.deletePhoto(photoId);

        // 3. 앨범에 사진이 더 없는 경우 -> 앨범도 삭제
        int remainingCount = albumMapper.countPhotos(albumId);
        if (remainingCount == 0) {
            albumMapper.deleteAlbum(albumId);
        }
        return "사진 삭제 성공";
    }

    @Transactional
    public String deleteSelectedPhotos(int userId, int tripId, List<PhotoDeleteReqDto> checkedPhotos) {

        try {
            // 권한 확인
           for (PhotoDeleteReqDto dto : checkedPhotos) {
               int ownerId = albumMapper.findOwner(tripId, dto.getPhotoId());
               if (ownerId != userId) {
                   return "하나 이상의 사진에 대해 삭제 권한이 없습니다.";
               }
           }

           // Firebase에서 사진 삭제 + DB에서 삭제
           Set<Integer> albumIdsToCheck = new HashSet<>();
           for (PhotoDeleteReqDto dto : checkedPhotos) {
               String photoUrl = albumMapper.getPhoto(dto.getPhotoId()).getPhotoUrl();

               boolean deletedFromFirebase = firebaseStorageService.deletePhotoFromFirebase(photoUrl);
               if (!deletedFromFirebase) {
                   return "일부 사진 삭제 실패 (Firebase)";
               }

               albumMapper.deletePhoto(dto.getPhotoId());
               albumIdsToCheck.add(dto.getAlbumId());
           }

           // 앨범에 사진이 남아 있는지 확인 후 없으면 앨범 삭제
           for (Integer albumId : albumIdsToCheck) {
               if (albumMapper.countPhotos(albumId) == 0) {
                   albumMapper.deleteAlbum(albumId);
               }
           }
           return "선택한 사진 삭제 성공";
        } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("사진 또는 앨범 삭제 중 오류가 발생했습니다.", e);
        }
    }

    public boolean editAlbumSchedule(int userId, AlbumCreateReqDto albumInfo){
        albumInfo.setUserId(userId);  // 여기서 userId 주입
        return albumMapper.editAlbumSchedule(albumInfo);
    }
}
