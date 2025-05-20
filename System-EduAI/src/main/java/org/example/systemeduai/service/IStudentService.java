package org.example.systemeduai.service;

import org.example.systemeduai.dto.request.CreateStudentRequest;
import org.example.systemeduai.dto.request.UpdateStudentRequest;
import org.example.systemeduai.dto.response.PageResponse;
import org.example.systemeduai.dto.response.StudentResponse;
import org.example.systemeduai.dto.student.StudentMeasurementUpdateDto;
import org.example.systemeduai.dto.student.StudentUpdateDto;
import org.example.systemeduai.dto.student.StudentUserDetailDto;
import org.example.systemeduai.model.Student;

public interface IStudentService {
    StudentResponse getStudentById(Integer studentId);
    PageResponse<StudentResponse> getStudents(int page, int size, String sortBy, String sortDir);
    PageResponse<StudentResponse> getStudentsByClassroomId(Integer classroomId, int page, int size, String sortBy, String sortDir);
//    void createStudent(CreateStudentRequest request);
    void updateStudent(UpdateStudentRequest request, Integer studentId);
    void deleteStudent(Integer studentId);
    void save(Student student);

    boolean existsByStudentPhone(String phone);

    Student findByAccountId(Integer accountId);

    StudentUserDetailDto findUserDetailByUsername(String username);

    StudentUserDetailDto updateStudentDetails(String username, StudentUpdateDto studentUpdateDto);

    StudentUserDetailDto updateStudentMeasurements(String username, StudentMeasurementUpdateDto measurementUpdateDto);
}
