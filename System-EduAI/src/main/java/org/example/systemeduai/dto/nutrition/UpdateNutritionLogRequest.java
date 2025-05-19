package org.example.systemeduai.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNutritionLogRequest {
    @NotNull
    private Boolean isCompleted;
    private String notes;
}
