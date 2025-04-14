package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.Album;
import com.sketchers.tripsketch_back.entity.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRespDto {
    private Album album;
    private List<Photo> photos;

    // 사진 개수 구하는 편의 메서드
    public int getPhotoCount() {
        return photos.size();
    }
}