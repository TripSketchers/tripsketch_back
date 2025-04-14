package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.AlbumRespDto;
import com.sketchers.tripsketch_back.entity.Album;
import com.sketchers.tripsketch_back.entity.Photo;
import com.sketchers.tripsketch_back.repository.AlbumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumMapper albumMapper;

    public List<AlbumRespDto> getPhotosAll(int userId, int tripId) {
        List<Album> albums = albumMapper.getAlbum(userId, tripId);
        List<AlbumRespDto> result = new ArrayList<>();

            for (Album album : albums) {
                // albumId로 해당 앨범의 사진 목록 가져오기
                List<Photo> photos = albumMapper.getPhotos(album.getAlbumId());

                // 앨범 + 사진 조립
                AlbumRespDto dto = new AlbumRespDto();
                dto.setAlbum(album);
                dto.setPhotos(photos);

                result.add(dto);
            }

        return result;
    }
//    public AlbumRespDto getAlbumFolders(int userId, int tripId) {
//        return new AlbumRespDto(albumMapper.getAlbumFolders(userId, tripId));
//    }
//    public AlbumRespDto getPhotosByFolder(int userId, int tripId, int albumId) {
//        return new AlbumRespDto(albumMapper.getPhotosByFolder(userId, tripId, albumId));
//    }
}
