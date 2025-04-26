package org.example.systemeduai.repository;

import org.example.systemeduai.model.LearningSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ILearningScheduleRepository extends JpaRepository<LearningSchedule, Integer> {
}
