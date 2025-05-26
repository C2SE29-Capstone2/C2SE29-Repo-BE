package org.example.systemeduai.service;

import org.example.systemeduai.dto.teacher.TeacherUpdateDto;
import org.example.systemeduai.dto.teacher.TeacherUserDetailDto;
import org.example.systemeduai.model.Teacher;

public interface ITeacherService {
    TeacherUserDetailDto findUserDetailByUsername(String username);

    TeacherUserDetailDto updateTeacherDetails(String username, TeacherUpdateDto updateDto);

    Teacher findTeacherByUsername(String username);
}
