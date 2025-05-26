package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.health.GrowthRecordDTO;
import org.example.systemeduai.model.ContactBook;
import org.example.systemeduai.model.GrowthRecord;
import org.example.systemeduai.model.Student;
import org.example.systemeduai.repository.IContactBookRepository;
import org.example.systemeduai.repository.IGrowthRecordRepository;
import org.example.systemeduai.repository.IStudentRepository;
import org.example.systemeduai.service.IGrowthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GrowthRecordServiceImpl implements IGrowthRecordService {

    @Autowired
    private IGrowthRecordRepository growthRecordRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IContactBookRepository contactBookRepository;

    @Override
    public GrowthRecordDTO saveGrowthRecord(GrowthRecordDTO growthRecordDTO) {
        if (growthRecordDTO.getStudentId() == null) {
            throw new IllegalArgumentException("Student ID must be provided");
        }


        Student student = studentRepository.findById(growthRecordDTO.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + growthRecordDTO.getStudentId()));

        GrowthRecord growthRecord = new GrowthRecord();
        growthRecord.setStudent(student);
        growthRecord.setMeasurementDate(growthRecordDTO.getMeasurementDate() != null
                ? growthRecordDTO.getMeasurementDate() : LocalDate.now());
        growthRecord.setHeight(growthRecordDTO.getHeight());
        growthRecord.setWeight(growthRecordDTO.getWeight());
        GrowthRecord savedRecord = growthRecordRepository.save(growthRecord);

        updateContactBookWithLatestGrowth(student.getStudentId());

        growthRecordDTO.setGrowthRecordId(savedRecord.getGrowthRecordId());
        growthRecordDTO.setStudentId(student.getStudentId());
        return growthRecordDTO;
    }

    @Override
    public GrowthRecordDTO updateGrowthRecord(Integer growthRecordId, GrowthRecordDTO growthRecordDTO) {
        if (growthRecordDTO.getStudentId() == null) {
            throw new IllegalArgumentException("Student ID must be provided");
        }

        Student student = studentRepository.findById(growthRecordDTO.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + growthRecordDTO.getStudentId()));

        GrowthRecord growthRecord = growthRecordRepository.findById(growthRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Growth record not found with ID: " + growthRecordId));

        growthRecord.setMeasurementDate(growthRecordDTO.getMeasurementDate() != null
                ? growthRecordDTO.getMeasurementDate() : LocalDate.now());
        growthRecord.setHeight(growthRecordDTO.getHeight());
        growthRecord.setWeight(growthRecordDTO.getWeight());
        GrowthRecord updatedRecord = growthRecordRepository.save(growthRecord);

        updateContactBookWithLatestGrowth(student.getStudentId());

        growthRecordDTO.setGrowthRecordId(updatedRecord.getGrowthRecordId());
        growthRecordDTO.setStudentId(student.getStudentId());
        return growthRecordDTO;

    }

    private void updateContactBookWithLatestGrowth(Integer studentId) {
        ContactBook contactBook = contactBookRepository.findByStudentStudentId(studentId)
                .orElse(new ContactBook());
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        contactBook.setStudent(student);

        List<GrowthRecord> growthRecords = growthRecordRepository.findByStudentStudentIdOrderByMeasurementDateDesc(studentId);
        if (!growthRecords.isEmpty()) {
            GrowthRecord latestRecord = growthRecords.getFirst();
            contactBook.setHeight(latestRecord.getHeight());
            contactBook.setWeight(latestRecord.getWeight());
        } else {
            contactBook.setHeight(null);
            contactBook.setWeight(null);
        }
        contactBookRepository.save(contactBook);
    }

    @Override
    public List<GrowthRecordDTO> getGrowthRecords(Integer studentId) {
        List<GrowthRecord> records = growthRecordRepository.findByStudentStudentIdOrderByMeasurementDateAsc(studentId);
        return records.stream().map(record -> {
            GrowthRecordDTO dto = new GrowthRecordDTO();
            dto.setGrowthRecordId(record.getGrowthRecordId());
            dto.setMeasurementDate(record.getMeasurementDate());
            dto.setHeight(record.getHeight());
            dto.setWeight(record.getWeight());
            dto.setStudentId(record.getStudent().getStudentId());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getGrowthChartData(Integer studentId) {
        List<GrowthRecord> records = growthRecordRepository.findByStudentStudentIdOrderByMeasurementDateAsc(studentId);
        return records.stream().map(record -> {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("date", record.getMeasurementDate().toString());
            dataPoint.put("height", record.getHeight());
            dataPoint.put("weight", record.getWeight());
            return dataPoint;
        }).collect(Collectors.toList());
    }
}
