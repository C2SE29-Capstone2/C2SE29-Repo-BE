package org.example.systemeduai.repository;

import org.example.systemeduai.model.Children;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface IChildrenRepository extends JpaRepository<Children, Integer> {
    List<Children> findByClassroomClassroomId(Integer classroomId);

    boolean existsByClassroomClassroomIdAndName(Integer classroomId, String name);
}
