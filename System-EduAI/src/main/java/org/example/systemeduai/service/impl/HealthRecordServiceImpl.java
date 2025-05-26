package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.health.HealthInfoDTO;
import org.example.systemeduai.model.ContactBook;
import org.example.systemeduai.model.HealthRecord;
import org.example.systemeduai.model.Student;
import org.example.systemeduai.repository.IContactBookRepository;
import org.example.systemeduai.repository.IHealthRecordRepository;
import org.example.systemeduai.repository.IStudentRepository;
import org.example.systemeduai.service.IHealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthRecordServiceImpl implements IHealthRecordService {

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IHealthRecordRepository healthRecordRepository;

    @Autowired
    private IContactBookRepository contactBookRepository;


    @Override
    public HealthInfoDTO saveHealthRecord(HealthInfoDTO healthInfoDTO) {
        if (healthInfoDTO == null || healthInfoDTO.getStudentId() == null) {
            throw new IllegalArgumentException("HealthInfoDTO and Student ID cannot be null");
        }

        Student student = studentRepository.findById(healthInfoDTO.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + healthInfoDTO.getStudentId()));

        HealthRecord healthRecord = healthRecordRepository.findByStudentStudentId(healthInfoDTO.getStudentId())
                .orElse(new HealthRecord());
        healthRecord.setStudent(student);
        healthRecord.setVaccinationSchedule(healthInfoDTO.getVaccinationSchedule());
        healthRecord.setMedicalHistory(healthInfoDTO.getMedicalHistory());
        healthRecord.setPeriodicCheck(healthInfoDTO.getPeriodicCheck());
        healthRecordRepository.save(healthRecord);

        ContactBook contactBook = contactBookRepository.findByStudentStudentId(healthInfoDTO.getStudentId())
                .orElse(new ContactBook());
        contactBook.setStudent(student);
        contactBook.setBloodGroup(healthInfoDTO.getBloodGroup());
        contactBook.setBloodPressure(healthInfoDTO.getBloodPressure());
        contactBook.setAllergies(healthInfoDTO.getAllergies());
        contactBook.setHeight(healthInfoDTO.getHeight());
        contactBook.setWeight(healthInfoDTO.getWeight());
        contactBookRepository.save(contactBook);

        return mapToHealthInfoDTO(healthRecord, contactBook, healthInfoDTO.getStudentId());
    }

    @Override
    public HealthInfoDTO getHealthRecord(Integer studentId) {
        HealthRecord healthRecord = healthRecordRepository.findByStudentStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Health record not found for student ID: " + studentId));
        ContactBook contactBook = contactBookRepository.findByStudentStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Contact book not found for student ID: " + studentId));
        return mapToHealthInfoDTO(healthRecord, contactBook, studentId);
    }
    private HealthInfoDTO mapToHealthInfoDTO(HealthRecord healthRecord, ContactBook contactBook, Integer studentId) {
        HealthInfoDTO dto = new HealthInfoDTO();
        dto.setVaccinationSchedule(healthRecord.getVaccinationSchedule());
        dto.setMedicalHistory(healthRecord.getMedicalHistory());
        dto.setPeriodicCheck(healthRecord.getPeriodicCheck());
        dto.setBloodGroup(contactBook.getBloodGroup());
        dto.setBloodPressure(contactBook.getBloodPressure());
        dto.setAllergies(contactBook.getAllergies());
        dto.setHeight(contactBook.getHeight());
        dto.setWeight(contactBook.getWeight());
        dto.setStudentId(studentId);
        return dto;
    }
}
