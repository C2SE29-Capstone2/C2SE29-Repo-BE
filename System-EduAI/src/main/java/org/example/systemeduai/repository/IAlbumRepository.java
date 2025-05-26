package org.example.systemeduai.repository;

import org.example.systemeduai.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface IAlbumRepository extends JpaRepository<Album, Integer> {
    List<Album> findByClassroomClassroomId(Integer classroomId);

    List<Album> findByActivityActivityId(Integer activityId);

    void deleteByActivityActivityId(Integer activityId);
}
