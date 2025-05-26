package org.example.systemeduai.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtracurricularActivityDTO {
    private Integer activityId;
    private String name;
    private String description;
    private Date activityDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String activityType;
    private Integer classroomId;
    private Integer teacherId;
}
