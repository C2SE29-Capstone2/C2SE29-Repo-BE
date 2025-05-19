package org.example.systemeduai.repository;

import org.example.systemeduai.model.NutritionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface INutritionLogRepository extends JpaRepository<NutritionLog, Integer> {
    List<NutritionLog> findByClassroomClassroomId(Integer classroomId);
}
