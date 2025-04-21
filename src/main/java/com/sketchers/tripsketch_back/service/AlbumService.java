package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.AlbumListRespDto;
import com.sketchers.tripsketch_back.dto.AlbumRespDto;
import com.sketchers.tripsketch_back.dto.AlbumUploadReqDto;
import com.sketchers.tripsketch_back.dto.TripScheduleRespDto;
import com.sketchers.tripsketch_back.entity.Album;
import com.sketchers.tripsketch_back.entity.Photo;
import com.sketchers.tripsketch_back.repository.AlbumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumMapper albumMapper;

    public AlbumListRespDto getPhotosAll(int userId, int tripId) {
        List<Album> albums = albumMapper.getAlbums(userId, tripId);
        List<AlbumRespDto> result = new ArrayList<>();

        for (Album album : albums) {
            List<Photo> photos = albumMapper.getPhotos(album.getAlbumId());
            AlbumRespDto albumRespDto = AlbumRespDto.builder()
                        .album(album)
                        .photos(photos)
                        .build();

            result.add(albumRespDto);
        }
        String startDate = albumMapper.getTripStartDate(userId, tripId);  // trip_tb에서 가져오기

        AlbumListRespDto albumListRespDto = AlbumListRespDto.builder()
                .startDate(startDate)
                .albums(result)
                .build();
        return albumListRespDto;
    }

    public AlbumListRespDto getAlbumFolders(int userId, int tripId) {
        List<Album> albums = albumMapper.getAlbums(userId, tripId);
        List<AlbumRespDto> result = new ArrayList<>();

        for (Album album : albums) {
            List<Photo> thumbnailPhoto = albumMapper.getThumbnailPhoto(album.getAlbumId());
            AlbumRespDto albumRespDto = AlbumRespDto.builder()
                    .album(album)
                    .photos(thumbnailPhoto)
                    .build();
            result.add(albumRespDto);
        }
        String startDate = albumMapper.getTripStartDate(userId, tripId);
        return new AlbumListRespDto(startDate, result);
    }

    public AlbumListRespDto getPhotosByFolder(int userId, int tripId, int albumId) {
        Album album = albumMapper.getAlbum(userId, tripId, albumId);
        List<Photo> photos = albumMapper.getPhotosByFolder(albumId);
        List<AlbumRespDto> result = new ArrayList<>();

        AlbumRespDto albumRespDto = AlbumRespDto.builder()
                            .album(album)
                            .photos(photos)
                            .build();
        result.add(albumRespDto);
        String startDate = albumMapper.getTripStartDate(userId, tripId);

        return new AlbumListRespDto(startDate, result);
    }

    public TripScheduleRespDto getTripSchedules(int userId, int tripId) {
        String startDate = albumMapper.getTripStartDate(userId, tripId);
        return new TripScheduleRespDto(startDate, albumMapper.getTripSchedules(userId, tripId));
    }

    @Transactional //실패 시 롤백
    public boolean createTripAlbum(int userId, int tripId, AlbumUploadReqDto albumUploadReqDto){
        try {
            //1. 해당 일정의 앨범이 존재하는지 검색
            int tripScheduleId = albumUploadReqDto.getTripScheduleId();
            int albumId = albumMapper.getAlbumId(userId, tripId, tripScheduleId);

            //2. 앨범이 없으면 새로 생성
            if(albumId == 0) {
                albumId = albumMapper.createTripAlbum(userId, tripId, tripScheduleId);
            }
            //3. 앨범에 사진 저장
            int successCount = 0;
            for (AlbumUploadReqDto.PhotoDto photoDto : albumUploadReqDto.getPhotos()) {
                int result = albumMapper.insertPhoto(albumId, photoDto.getPhotoUrl(), photoDto.getMemo());
                if (result > 0) {
                    successCount++;
                } else {
                    throw new RuntimeException("사진 저장 실패"); // 실패 발생 시 강제 롤백
                }
            }
            //4. 저장된 사진 수와 요청한 사진 수가 같으면 성공
            if (successCount == albumUploadReqDto.getPhotos().size()) {
                return true;
            } else {
                throw new RuntimeException("사진 일부 저장 실패");
            }
        } catch (Exception e) {
            // 여기서 로그를 남기거나, custom 예외로 변환 가능
            throw new RuntimeException("앨범 업로드 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public boolean editPhotoMemo(int photoId, String memo) {
        return albumMapper.editPhotoMemo(photoId, memo);
    }
}
