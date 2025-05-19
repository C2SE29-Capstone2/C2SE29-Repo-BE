package org.example.systemeduai.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.model.enums.MealTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionMenuDTO {
    private Integer nutritionMenuId;
    private MealTime mealTime;
    private String mealDetails;
    private String healthCondition;
    private Integer classroomId;
}
