package org.example.systemeduai.service;

import org.example.systemeduai.dto.nutrition.CreateNutritionLogRequest;
import org.example.systemeduai.dto.nutrition.NutritionLogDTO;
import org.example.systemeduai.dto.nutrition.UpdateNutritionLogRequest;

import java.util.List;

public interface INutritionLogService {
    NutritionLogDTO trackMeal(CreateNutritionLogRequest request);

    NutritionLogDTO updateMealCompletion(Integer logId, UpdateNutritionLogRequest request);

    List<NutritionLogDTO> getNutritionLogs(Integer classroomId);
}
