package org.example.systemeduai.repository;

import org.example.systemeduai.model.ExtracurricularActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface IExtracurricularActivityRepository extends JpaRepository<ExtracurricularActivity, Integer> {
    List<ExtracurricularActivity> findByClassroomClassroomId(Integer classroomId);
}
