package org.example.systemeduai.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.model.enums.MealTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReminderRequest {
    @NotNull
    private Integer classroomId;
    @NotNull
    private MealTime mealTime;
    @NotBlank
    private String mealDetails;
}
