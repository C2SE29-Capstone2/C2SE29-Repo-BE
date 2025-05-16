package org.example.systemeduai.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentMeasurementUpdateDto {
    @NotNull(message = "Height cannot be null")
    @DecimalMin(value = "50.0", message = "Height must be at least 50 cm")
    private Double height;

    @NotNull(message = "Weight cannot be null")
    @DecimalMin(value = "5.0", message = "Weight must be at least 5 kg")
    private Double weight;
}