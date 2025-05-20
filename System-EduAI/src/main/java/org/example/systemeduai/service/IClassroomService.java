package org.example.systemeduai.service;

import org.example.systemeduai.dto.request.CreateClassroomRequest;
import org.example.systemeduai.dto.request.UpdateClassroomRequest;
import org.example.systemeduai.dto.response.ClassroomResponse;

import java.util.List;

public interface IClassroomService {
    List<ClassroomResponse> getAllClassrooms();
    void createClassroom(CreateClassroomRequest request);
    void updateClassroom(Integer classroomId, UpdateClassroomRequest request);
    void deleteClassroom(Integer classroomId);
    void addTeachersToClassroom(Integer classroomId, List<Integer> teacherId, String role);
    void removeTeachersFromClassroom(Integer classroomId, List<Integer> teacherId);
    List<ClassroomResponse> getClassroomsByTeacherId(Integer teacherId);

}
