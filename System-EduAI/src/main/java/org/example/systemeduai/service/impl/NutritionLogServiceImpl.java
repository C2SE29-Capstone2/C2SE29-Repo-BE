package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.nutrition.CreateNutritionLogRequest;
import org.example.systemeduai.dto.nutrition.NutritionLogDTO;
import org.example.systemeduai.dto.nutrition.UpdateNutritionLogRequest;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.NutritionLog;
import org.example.systemeduai.model.NutritionMenu;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.repository.INutritionLogRepository;
import org.example.systemeduai.repository.INutritionMenuRepository;
import org.example.systemeduai.service.INutritionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NutritionLogServiceImpl implements INutritionLogService {

    @Autowired
    private IClassroomRepository classroomRepository;

    @Autowired
    private INutritionMenuRepository nutritionMenuRepository;

    @Autowired
    private INutritionLogRepository nutritionLogRepository;

    @Override
    public NutritionLogDTO trackMeal(CreateNutritionLogRequest request) {
        Classroom classroom = classroomRepository.findById(request.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + request.getClassroomId()));

        NutritionMenu nutritionMenu = null;
        if (request.getNutritionMenuId() != null) {
            nutritionMenu = nutritionMenuRepository.findById(request.getNutritionMenuId())
                    .orElseThrow(() -> new RuntimeException("Nutrition menu not found with ID: " + request.getNutritionMenuId()));
        }

        NutritionLog log = new NutritionLog();
        log.setClassroom(classroom);
        log.setMealTime(request.getMealTime());
        log.setMealDetails(request.getMealDetails());
        log.setNutritionMenu(nutritionMenu);
        log.setLogTime(LocalDateTime.now());
        log.setCompleted(false);
        log.setNotes(request.getNotes());

        NutritionLog savedLog = nutritionLogRepository.save(log);
        return convertToNutritionLogDTO(savedLog);
    }

    @Override
    public NutritionLogDTO updateMealCompletion(Integer logId, UpdateNutritionLogRequest request) {
        NutritionLog log = nutritionLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Nutrition log not found with ID: " + logId));
        log.setCompleted(request.getIsCompleted());
        log.setNotes(request.getNotes());
        NutritionLog updatedLog = nutritionLogRepository.save(log);
        return convertToNutritionLogDTO(updatedLog);
    }

    @Override
    public List<NutritionLogDTO> getNutritionLogs(Integer classroomId) {
        if (classroomId == null) {
            throw new IllegalArgumentException("Classroom ID must not be null");
        }
        return nutritionLogRepository.findByClassroomClassroomId(classroomId)
                .stream()
                .map(this::convertToNutritionLogDTO)
                .collect(Collectors.toList());
    }

    private NutritionLogDTO convertToNutritionLogDTO(NutritionLog log) {
        NutritionLogDTO dto = new NutritionLogDTO();
        dto.setNutritionLogId(log.getNutritionLogId());
        dto.setMealTime(log.getMealTime());
        dto.setMealDetails(log.getMealDetails());
        dto.setLogTime(log.getLogTime());
        dto.setCompleted(log.isCompleted());
        dto.setNotes(log.getNotes());
        dto.setNutritionMenuId(log.getNutritionMenu() != null ? log.getNutritionMenu().getNutritionMenuId() : null);
        dto.setClassroomId(log.getClassroom().getClassroomId());
        return dto;
    }
}
