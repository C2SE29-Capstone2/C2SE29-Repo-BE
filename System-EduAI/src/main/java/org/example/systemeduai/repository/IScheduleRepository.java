package org.example.systemeduai.repository;

import org.example.systemeduai.enums.ActivityType;
import org.example.systemeduai.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Repository
@Transactional
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByTimeSlot(Date timeSlot);
    List<Schedule> findByTimeSlotAndClassroom_ClassroomId(Date timeSlot, Integer classroomId);
}
