package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.chat.ClassroomMembersMessageDTO;
import org.example.systemeduai.dto.chat.ParentMessageDTO;
import org.example.systemeduai.dto.chat.TeacherMessageDTO;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.service.IClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements IClassroomService {

    @Autowired
    private IClassroomRepository classroomRepository;

    @Override
    public Optional<Classroom> getClassroomById(Integer classId) {
        return classroomRepository.findById(classId);
    }

    @Override
    public boolean existsByClassroomId(Integer classId) {
        return classroomRepository.existsByClassroomId(classId);
    }

    @Override
    public ClassroomMembersMessageDTO getClassroomMembers(Integer classroomId) {
        Optional<Classroom> classroomOpt = getClassroomById(classroomId);
        if (!classroomOpt.isPresent()) {
            throw new RuntimeException("Classroom not found");
        }

        Classroom classroom = classroomOpt.get();

        List<Object[]> teacherResults = classroomRepository.findTeachersByClassroomId(classroomId);
        List<TeacherMessageDTO> teachers = teacherResults.stream()
                .map(result -> new TeacherMessageDTO(
                        (Integer) result[0],
                        (String) result[1],
                        (String) result[2],
                        (String) result[3],
                        (String) result[4]
                ))
                .collect(Collectors.toList());

        List<Object[]> parentResults = classroomRepository.findParentsByClassroomId(classroomId);
        List<ParentMessageDTO> parents = parentResults.stream()
                .map(result -> new ParentMessageDTO(
                        (Integer) result[0],
                        (String) result[1],
                        (Integer) result[2],
                        (String) result[3]
                ))
                .collect(Collectors.toList());

        return new ClassroomMembersMessageDTO(
                classroom.getClassroomId(),
                classroom.getClassroomName(),
                teachers,
                parents
        );
    }
}
