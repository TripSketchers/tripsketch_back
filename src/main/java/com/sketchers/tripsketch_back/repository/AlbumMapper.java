package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.dto.AlbumUploadReqDto;
import com.sketchers.tripsketch_back.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlbumMapper {
    public List<Album> getAlbums(int userId, int tripId);
    public List<Photo> getPhotos(int albumId);
    public String getTripStartDate(int userId, int tripId);
    public List<Photo> getThumbnailPhoto(int tripId);
    public Album getAlbum(int userId, int tripId, int albumId);
    public List<Photo> getPhotosByFolder(int albumId);
//    public List<String> getTripScheduleDates(int userId, int tripId);
    public List<TripSchedule> getTripSchedules(int userId, int tripId);
    public int getAlbumId(int userId, int tripId, int tripScheduleId);
    public int createTripAlbum(int userId, int tripId, int tripScheduleId);
    public int insertPhoto(int albumId, String photoUrl, String memo);
    public boolean editPhotoMemo(int photoId, String memo);
}
