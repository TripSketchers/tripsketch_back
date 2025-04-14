package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlbumMapper {
    public List<Album> getAlbum(int userId, int tripId);
    public List<Photo> getPhotos(int tripId);
    //    public Album getAlbumFolders(int userId, int tripId);
    //    public Album getPhotosByFolder(int userId, int tripId, int albumId);
}
