package org.example.systemeduai.service;

import org.example.systemeduai.dto.schedule.AlbumDTO;

import java.util.List;

public interface IAlbumService {
    AlbumDTO createAlbum(AlbumDTO dto, String username);

    AlbumDTO getAlbumById(Integer id);

    List<AlbumDTO> getAlbumsByClassroom(Integer classroomId);

    List<AlbumDTO> getAlbumsByActivity(Integer activityId);

    AlbumDTO updateAlbum(Integer id, AlbumDTO dto, String username);

    void deleteAlbum(Integer id, String username);
}
