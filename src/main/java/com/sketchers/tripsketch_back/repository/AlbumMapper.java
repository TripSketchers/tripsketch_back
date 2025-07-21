package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.dto.AlbumCreateReqDto;
import com.sketchers.tripsketch_back.dto.AlbumUploadReqDto;
import com.sketchers.tripsketch_back.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlbumMapper {
    public List<Album> getAlbums(int userId, int tripId);
    public List<Photo> getPhotosByAlbumId(int albumId);
    public Trip getTripInfo(int userId, int tripId);
    public List<TripSchedulePlaceView> getTripSchedules(int userId, int tripId);
    public int getAlbumId(int userId, int tripId, String date, String placeName, String startTime);
    public int createTripAlbum(AlbumCreateReqDto request);
    public int insertPhoto(int albumId, String photoUrl, String memo);
    public boolean editPhotoMemo(int photoId, String memo);
    public boolean deleteAlbum(int albumId);
    public boolean deletePhoto(int albumId);
    public int deleteSelectedPhotos(List<Integer> checkedPhoto);
    public int findAlbumId(int photoId);
    public int countPhotos(int albumId);
    public int findOwner(int tripId, int photoId);
    public Photo getPhoto(int photoId);
    public boolean editAlbumSchedule(AlbumCreateReqDto albumCreateReqDto);
    public int getAlbumTripScheduleId(int tripId, int albumId);
}
