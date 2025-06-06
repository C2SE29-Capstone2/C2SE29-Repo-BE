package org.example.systemeduai.repository;

import org.example.systemeduai.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface IAttendanceRepository extends JpaRepository<Attendance, Integer> {
}
