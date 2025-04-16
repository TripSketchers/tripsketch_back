package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.AlbumListRespDto;
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
}
