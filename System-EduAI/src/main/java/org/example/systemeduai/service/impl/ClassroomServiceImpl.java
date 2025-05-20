package org.example.systemeduai.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.systemeduai.dto.internal.ClassroomTeacherInfo;
import org.example.systemeduai.dto.internal.CreateClassroomTeacherInfo;
import org.example.systemeduai.dto.request.CreateClassroomRequest;
import org.example.systemeduai.dto.request.UpdateClassroomRequest;
import org.example.systemeduai.dto.response.ClassroomResponse;
import org.example.systemeduai.dto.response.ModifyTeacherClassroomResponse;
import org.example.systemeduai.enums.ClassroomType;
import org.example.systemeduai.exception.ResourceNotFoundException;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.ClassroomTeacher;
import org.example.systemeduai.model.Teacher;
import org.example.systemeduai.repository.*;
import org.example.systemeduai.service.IClassroomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomServiceImpl implements IClassroomService {
    private final IClassroomRepository classroomRepository;
    private final IClassroomTeacherRepository classroomTeacherRepository;
    private final ITeacherRepository teacherRepository;
    private final ICameraRepository cameraRepository;
    private final IStudentRepository studentRepository;

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
        List<Integer> teacherIds = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        request.getTeacherInfoList()
                .forEach(teacherInfo -> {
                    teacherIds.add(teacherInfo.getTeacherId());
                    roles.add(teacherInfo.getRole());
                });
        List<Teacher> teachers = teacherRepository.findByTeacherIdIn(teacherIds);
        if (teachers.size() != teacherIds.size()) {
            throw new IllegalArgumentException("Some teachers not found");
        }
        if (teachers.size() != roles.size()) {
            throw new IllegalArgumentException("Teachers and roles size mismatch");
        }
        Classroom classroom = Classroom.builder()
                .classroomName(request.getClassroomName())
                .classroomType(ClassroomType.valueOf(request.getClassroomType()))
                .build();
        classroom = classroomRepository.save(classroom);
        for (int i = 0; i < teachers.size(); i++) {
            classroomTeacherRepository.save(ClassroomTeacher.builder()
                    .classroom(classroom)
                    .teacher(teachers.get(i))
                    .role(roles.get(i))
                    .build());
        }
    }

    @Override
    public void updateClassroom(Integer classroomId, UpdateClassroomRequest request) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom", "Id", classroomId));
        if (request.getClassroomName() != null) {
            classroom.setClassroomName(request.getClassroomName());
        }
        if (request.getClassroomType() != null) {
            classroom.setClassroomType(ClassroomType.valueOf(request.getClassroomType()));
        }
        classroomRepository.save(classroom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClassroom(Integer classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom", "Id", classroomId));
        cameraRepository.removeCameraFromClassroomByClassroomId(classroomId);
        studentRepository.removeStudentFromClassroomByClassroomId(classroomId);
        classroomTeacherRepository.deleteByClassroomClassroomId(classroomId);
        classroomRepository.delete(classroom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ModifyTeacherClassroomResponse> addTeachersToClassroom(Integer classroomId,
                                                                       List<CreateClassroomTeacherInfo> teacherInfoList){
        List<ModifyTeacherClassroomResponse> modifyTeacherClassroomResponses = new ArrayList<>();
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom", "Id", classroomId));
        List<Integer> teacherIds = new ArrayList<>();
        Map<Integer, String> teacherIdRoleMap = new HashMap<>();
        teacherInfoList.forEach(teacherInfo -> {
            teacherIds.add(teacherInfo.getTeacherId());
            teacherIdRoleMap.put(teacherInfo.getTeacherId(), teacherInfo.getRole());
        });

        List<Teacher> teachers = teacherRepository.findByTeacherIdIn(teacherIds);
        if (teachers.size() != teacherIds.size()) {
            throw new IllegalArgumentException("Some teachers not found");
        }
//        Set<Integer> existingTeacherIds = classroomTeacherRepository.findByTeacherTeacherIdIn(teacherIds)
//                .stream()
//                .map(classroomTeacher -> classroomTeacher.getTeacher().getTeacherId())
//                .collect(Collectors.toSet());
        Set<Integer> existingTeacherIds = classroomTeacherRepository.findDistinctTeacherIdsByTeacherIdAndClassroomId(
                teacherIds,
                classroomId);

        teachers.forEach(teacher ->{
            if (existingTeacherIds.contains(teacher.getTeacherId())) {
                modifyTeacherClassroomResponses.add(ModifyTeacherClassroomResponse.builder()
                        .teacherId(teacher.getTeacherId())
                        .teacherName(teacher.getTeacherName())
                        .isAdded(false)
                        .build());
            } else {
                classroomTeacherRepository.save(ClassroomTeacher.builder()
                        .classroom(classroom)
                        .teacher(teacher)
                        .role(teacherIdRoleMap.get(teacher.getTeacherId()))
                        .build());
                modifyTeacherClassroomResponses.add(ModifyTeacherClassroomResponse.builder()
                        .teacherId(teacher.getTeacherId())
                        .teacherName(teacher.getTeacherName())
                        .isAdded(true)
                        .build());
            }
        });
        return modifyTeacherClassroomResponses;
    }

    @Override
    public List<ModifyTeacherClassroomResponse> removeTeachersFromClassroom(Integer classroomId,
                                                                            List<Integer> teacherIds) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom", "Id", classroomId));
        List<ClassroomTeacher> classroomTeachers = classroomTeacherRepository.findByClassroomClassroomId(classroomId);
        Set<Integer> existingTeacherIds = classroomTeachers.stream()
                .map(classroomTeacher -> classroomTeacher.getTeacher().getTeacherId())
                .collect(Collectors.toSet());
        List<ModifyTeacherClassroomResponse> modifyTeacherClassroomResponses = new ArrayList<>();
        teacherIds.forEach(teacherId -> {
            if (existingTeacherIds.contains(teacherId)) {
                classroomTeacherRepository.deleteByClassroomClassroomIdAndTeacherTeacherId(classroomId, teacherId);
                modifyTeacherClassroomResponses.add(ModifyTeacherClassroomResponse.builder()
                        .teacherId(teacherId)
                        .isAdded(true)
                        .build());
            } else {
                modifyTeacherClassroomResponses.add(ModifyTeacherClassroomResponse.builder()
                        .teacherId(teacherId)
                        .isAdded(false)
                        .build());
            }
        });
        return modifyTeacherClassroomResponses;
    }

    @Override
    public List<ClassroomResponse> getClassroomsByTeacherId(Integer teacherId) {
        List<ClassroomTeacher> classroomTeachers = classroomTeacherRepository.findByTeacherTeacherId(teacherId);
        List<ClassroomTeacher> classroomTeachersByClassroomId = classroomTeacherRepository.findByClassroomClassroomIdIn(
                classroomTeachers.stream()
                        .map(classroomTeacher -> classroomTeacher.getClassroom().getClassroomId())
                        .collect(Collectors.toList()));
        Map<Integer, List<ClassroomTeacherInfo>> classroomTeacherMap = classroomTeachersByClassroomId.stream()
                .collect(Collectors.groupingBy(
                        classroomTeacher -> classroomTeacher.getClassroom().getClassroomId(),
                        Collectors.mapping(classroomTeacher -> ClassroomTeacherInfo.builder()
                                                   .teacherName(classroomTeacher.getTeacher().getTeacherName())
                                                   .teacherId(classroomTeacher.getTeacher().getTeacherId())
                                                   .role(classroomTeacher.getRole())
                                                   .build(),
                                           Collectors.toList())
                ));
        return classroomTeachers.stream()
                .map(classroomTeacher -> ClassroomResponse.builder()
                        .classroomId(classroomTeacher.getClassroom().getClassroomId())
                        .classroomName(classroomTeacher.getClassroom().getClassroomName())
                        .classroomType(classroomTeacher.getClassroom().getClassroomType().name())
                        .classroomTeachers(classroomTeacherMap.get(classroomTeacher.getClassroom().getClassroomId()))
                        .build())
                .collect(Collectors.toList());
    }
}
