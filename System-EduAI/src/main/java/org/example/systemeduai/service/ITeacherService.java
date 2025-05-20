package org.example.systemeduai.service;

import org.example.systemeduai.dto.request.CreateTeacherRequest;
import org.example.systemeduai.dto.request.UpdateTeacherRequest;
import org.example.systemeduai.dto.response.PageResponse;
import org.example.systemeduai.dto.response.TeacherResponse;

import java.util.List;

public interface ITeacherService {
    TeacherResponse getTeacherById(Integer teacherId);
    PageResponse<TeacherResponse> getTeachers(int page, int size, String sortBy, String sortDir);
    void createTeacher(CreateTeacherRequest request);
    void updateTeacher(Integer teacherId, UpdateTeacherRequest request);
    void deleteTeacher(Integer teacherId);
}
