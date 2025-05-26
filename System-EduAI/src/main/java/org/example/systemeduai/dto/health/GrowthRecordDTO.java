package org.example.systemeduai.dto.health;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrowthRecordDTO {
    private Integer growthRecordId;
    private LocalDate measurementDate;
    private Double height;
    private Double weight;
    private Integer studentId;
}
