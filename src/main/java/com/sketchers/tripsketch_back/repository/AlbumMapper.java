package com.sketchers.tripsketch_back.repository;

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
}
