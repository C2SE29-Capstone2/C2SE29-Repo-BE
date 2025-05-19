package org.example.systemeduai.service;


import org.example.systemeduai.dto.nutrition.NutritionMenuDTO;

import java.util.List;

public interface INutritionMenuService {
    List<NutritionMenuDTO> suggestMealPlan(Integer classroomId, String healthCondition);
}
