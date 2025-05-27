package org.example.systemeduai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.systemeduai.model.Children;
import org.example.systemeduai.repository.IChildrenRepository;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.service.IChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChildrenServiceImpl implements IChildrenService {

    @Autowired
    private IChildrenRepository childrenRepository;

    @Autowired
    private IClassroomRepository classroomRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void addChild(Integer classId, String name, List<Double> embedding) throws JsonProcessingException {
        if (!classroomRepository.existsByClassroomId(classId)) {
            throw new IllegalArgumentException("Classroom with ID " + classId + " does not exist");
        }
        if (childrenRepository.existsByClassroomClassroomIdAndName(classId, name)) {
            throw new IllegalArgumentException("Child " + name + " already exists in class " + classId);
        }

        Children child = new Children();
        child.setName(name);
        child.setEmbedding(objectMapper.writeValueAsString(embedding));
        child.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        child.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        child.setClassroom(classroomRepository.findById(classId).orElse(null));

        childrenRepository.save(child);
    }

    @Override
    public Map<String, Object> getChildrenByClassId(Integer classId) {
        if (!classroomRepository.existsByClassroomId(classId)) {
            throw new IllegalArgumentException("Classroom with ID " + classId + " does not exist");
        }

        List<Children> children = childrenRepository.findByClassroomClassroomId(classId);
        Map<String, List<Double>> sampleData = children.stream()
                .limit(3)
                .collect(Collectors.toMap(
                        Children::getName,
                        child -> {
                            try {
                                List<Double> embedding = objectMapper.readValue(child.getEmbedding(), List.class);
                                return embedding.subList(0, Math.min(5, embedding.size()));
                            } catch (Exception e) {
                                return List.of();
                            }
                        }
                ));

        return Map.of(
                "totalEntries", children.size(),
                "keys", children.stream().map(Children::getName).collect(Collectors.toList()),
                "sampleData", sampleData
        );
    }
}
