package org.example.systemeduai.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.enums.ActivityType;

import java.sql.Date;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private Integer scheduleId;
    private String name;
    private Date date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ActivityType activityType;
    private Integer classroomId;
    private Integer teacherId;
}
