package org.example.systemeduai.repository;

import org.example.systemeduai.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface IHealthRecordRepository extends JpaRepository<HealthRecord, Integer> {
    Optional<HealthRecord> findByStudentStudentId(Integer studentId);
}
