package org.example.systemeduai.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.enums.MealTime;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionLogDTO {
    private Integer nutritionLogId;
    private MealTime mealTime;
    private String mealDetails;
    private LocalDateTime logTime;
    private boolean isCompleted;
    private String notes;
    private Integer nutritionMenuId;
    private Integer classroomId;
}
