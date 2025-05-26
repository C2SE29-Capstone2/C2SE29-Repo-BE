package org.example.systemeduai.repository;

import org.example.systemeduai.model.GrowthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface IGrowthRecordRepository extends JpaRepository<GrowthRecord, Integer> {
    List<GrowthRecord> findByStudentStudentIdOrderByMeasurementDateAsc(Integer studentId);
    List<GrowthRecord> findByStudentStudentIdOrderByMeasurementDateDesc(Integer studentId);
}
