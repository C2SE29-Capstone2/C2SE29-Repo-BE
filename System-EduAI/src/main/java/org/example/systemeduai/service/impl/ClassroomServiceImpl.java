package org.example.systemeduai.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.systemeduai.dto.internal.ClassroomTeacherInfo;
import org.example.systemeduai.dto.request.CreateClassroomRequest;
import org.example.systemeduai.dto.request.UpdateClassroomRequest;
import org.example.systemeduai.dto.response.ClassroomResponse;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.repository.IClassroomTeacherRepository;
import org.example.systemeduai.repository.ITeacherRepository;
import org.example.systemeduai.service.IClassroomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomServiceImpl implements IClassroomService {
    private final IClassroomRepository classroomRepository;
    private final IClassroomTeacherRepository classroomTeacherRepository;
    private final ITeacherRepository teacherRepository;

    @Override
    public List<ClassroomResponse> getAllClassrooms() {
        Map<Integer, List<ClassroomTeacherInfo>> classroomTeacherMap = classroomTeacherRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        classroomTeacher -> classroomTeacher.getClassroom().getClassroomId(),
                        Collectors.mapping(classroomTeacher -> ClassroomTeacherInfo.builder()
                                                   .teacherName(classroomTeacher.getTeacher().getTeacherName())
                                                   .teacherId(classroomTeacher.getTeacher().getTeacherId())
                                                   .role(classroomTeacher.getRole())
                                                   .build(),
                                           Collectors.toList())
                ));
        List<Classroom> classrooms = classroomRepository.findAll();
        return classrooms.stream()
                .map(classroom -> ClassroomResponse.builder()
                        .classroomId(classroom.getClassroomId())
                        .classroomName(classroom.getClassroomName())
                        .classroomType(classroom.getClassroomType().name())
                        .classroomTeachers(classroomTeacherMap.get(classroom.getClassroomId()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void createClassroom(CreateClassroomRequest request) {

    }

    @Override
    public void updateClassroom(Integer classroomId, UpdateClassroomRequest request) {

    }

    @Override
    public void deleteClassroom(Integer classroomId) {

    }

    @Override
    public void addTeachersToClassroom(Integer classroomId, List<Integer> teacherId, String role) {

    }

    @Override
    public void removeTeachersFromClassroom(Integer classroomId, List<Integer> teacherId) {

    }

    @Override
    public List<ClassroomResponse> getClassroomsByTeacherId(Integer teacherId) {
        return null;
    }
}
