package org.example.systemeduai.service;

import org.example.systemeduai.dto.chat.ClassroomMembersMessageDTO;
import org.example.systemeduai.dto.classroom.ClassroomDto;
import org.example.systemeduai.model.Classroom;

import java.util.List;
import java.util.Optional;

public interface IClassroomService {
    Optional<Classroom> getClassroomById(Integer classId);

    boolean existsByClassroomId(Integer classId);

    ClassroomMembersMessageDTO getClassroomMembers(Integer classroomId);
}
