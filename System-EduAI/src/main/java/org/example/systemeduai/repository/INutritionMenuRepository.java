package org.example.systemeduai.repository;

import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.NutritionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface INutritionMenuRepository extends JpaRepository<NutritionMenu, Integer> {
    List<NutritionMenu> findByClassroomAndHealthCondition(Classroom classroom, String healthCondition);
}
