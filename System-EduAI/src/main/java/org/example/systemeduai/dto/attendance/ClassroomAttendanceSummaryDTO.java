package org.example.systemeduai.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomAttendanceSummaryDTO {
    private Integer classroomId;
    private String classroomName;
    private String classroomType;
    private Integer totalStudents;
    private Integer presentStudents;
    private Integer absentStudents;
}
