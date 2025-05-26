package org.example.systemeduai.dto.health;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthInfoDTO {
    private Integer studentId;
    private LocalDate vaccinationSchedule;
    private String medicalHistory;
    private String periodicCheck;
    private String allergies;
    private String bloodGroup;
    private String bloodPressure;
    private Double height;
    private Double weight;
}
