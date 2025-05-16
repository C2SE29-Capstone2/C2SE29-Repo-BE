package org.example.systemeduai.service;

import org.example.systemeduai.dto.student.StudentMeasurementUpdateDto;
import org.example.systemeduai.dto.student.StudentUpdateDto;
import org.example.systemeduai.dto.student.StudentUserDetailDto;
import org.example.systemeduai.model.Student;

public interface IStudentService {

    void save(Student student);

    boolean existsByStudentPhone(String phone);

    Student findByAccountId(Integer accountId);

    StudentUserDetailDto findUserDetailByUsername(String username);

    StudentUserDetailDto updateStudentDetails(String username, StudentUpdateDto studentUpdateDto);

    StudentUserDetailDto updateStudentMeasurements(String username, StudentMeasurementUpdateDto measurementUpdateDto);
}
