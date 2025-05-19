package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.nutrition.NutritionMenuDTO;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.NutritionMenu;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.repository.INutritionMenuRepository;
import org.example.systemeduai.service.INutritionMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NutritionMenuServiceImpl implements INutritionMenuService {

    @Autowired
    private IClassroomRepository classroomRepository;

    @Autowired
    private INutritionMenuRepository nutritionMenuRepository;

    @Override
    public List<NutritionMenuDTO> suggestMealPlan(Integer classroomId, String healthCondition) {
        if (classroomId == null || healthCondition == null || healthCondition.trim().isEmpty()) {
            throw new IllegalArgumentException("Classroom ID and health condition must not be null or empty");
        }

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + classroomId));

        return nutritionMenuRepository.findByClassroomAndHealthCondition(classroom, healthCondition)
                .stream()
                .map(this::convertToNutritionMenuDTO)
                .collect(Collectors.toList());
    }

    private NutritionMenuDTO convertToNutritionMenuDTO(NutritionMenu menu) {
        NutritionMenuDTO dto = new NutritionMenuDTO();
        dto.setNutritionMenuId(menu.getNutritionMenuId());
        dto.setMealTime(menu.getMealTime());
        dto.setMealDetails(menu.getMealDetails());
        dto.setHealthCondition(menu.getHealthCondition());
        dto.setClassroomId(menu.getClassroom().getClassroomId());
        return dto;
    }

}
