package org.example.systemeduai.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceDTO {
    private Integer studentId;
    private String studentName;
    private String profileImage;
    private String attendanceStatus;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String classroomName;
}
