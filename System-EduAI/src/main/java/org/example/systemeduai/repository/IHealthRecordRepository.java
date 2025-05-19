package org.example.systemeduai.repository;

import org.example.systemeduai.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface IHealthRecordRepository extends JpaRepository<HealthRecord, Integer> {
    Optional<HealthRecord> findByStudentStudentId(Integer studentId);
    @Modifying
    @Transactional
    void deleteByStudentStudentId(Integer studentId);
}
