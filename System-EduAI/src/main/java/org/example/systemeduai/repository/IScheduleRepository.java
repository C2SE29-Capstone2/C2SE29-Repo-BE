package org.example.systemeduai.repository;

import org.example.systemeduai.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
}
