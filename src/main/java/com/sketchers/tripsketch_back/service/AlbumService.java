package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.*;
import com.sketchers.tripsketch_back.entity.Album;
import com.sketchers.tripsketch_back.entity.Photo;
import com.sketchers.tripsketch_back.repository.AlbumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumMapper albumMapper;

    public AlbumListRespDto getAlbums(int userId, int tripId) {
        List<Album> albums = albumMapper.getAlbums(userId, tripId);
        String startDate = albumMapper.getTripStartDate(userId, tripId);  // trip_tb에서 가져오기

        AlbumListRespDto albumListRespDto = AlbumListRespDto.builder()
                .startDate(startDate)
                .albums(albums)
                .build();
        return albumListRespDto;
    }

    public PhotoRespDto getPhotosByAlbumId(int albumId) {
        List<Photo> photos = albumMapper.getPhotosByAlbumId(albumId);

        return  PhotoRespDto.builder()
                .photos(photos)
                .build();
    }

    public TripScheduleRespDto getTripSchedules(int userId, int tripId) {
        return new TripScheduleRespDto(albumMapper.getTripSchedules(userId, tripId));
    }

    @Transactional //실패 시 롤백
    public boolean createTripAlbum(int userId, int tripId, AlbumUploadReqDto albumUploadReqDto){
        try {
            //1. 해당 일정의 앨범이 존재하는지 검색
            int tripScheduleId = albumUploadReqDto.getTripScheduleId();
            int albumId = albumMapper.getAlbumId(userId, tripId, tripScheduleId);

            //2. 앨범이 없으면 새로 생성
            if(albumId == 0) {
                AlbumCreateReqDto request = AlbumCreateReqDto.builder()
                        .userId(userId)
                        .tripId(tripId)
                        .tripScheduleId(tripScheduleId)
                        .build();
                albumMapper.createTripAlbum(request);

                albumId = request.getAlbumId();  // 여기서 새로 생성된 albumId 가져옴
                System.out.println("새로 생성된 albumId: " + albumId);

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
    public boolean deletePhoto(int tripId, int photoId) {
        // 1. 앨범 ID 조회 (삭제 전 album_id 확보)
        int albumId = albumMapper.findAlbumId(photoId);

        // 2. 사진 삭제
        albumMapper.deletePhoto(photoId);

        // 3. 앨범에 사진이 더 없는 경우 -> 앨범도 삭제
        int remainingCount = albumMapper.countPhotos(albumId);
        if (remainingCount == 0) {
            albumMapper.deleteAlbum(albumId);
        }
        return true;
    }

    @Transactional
    public boolean deleteSelectedPhotos(List<Integer> checkedPhoto) {

        return albumMapper.deleteSelectedPhotos(checkedPhoto) > 0;
    }
}
