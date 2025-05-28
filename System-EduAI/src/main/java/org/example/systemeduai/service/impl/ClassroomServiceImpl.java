package org.example.systemeduai.service.impl;

import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.service.IClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
