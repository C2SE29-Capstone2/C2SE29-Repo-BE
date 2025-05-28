package org.example.systemeduai.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Integer studentId;
    private String studentName;
    private String attendanceStatus;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}
