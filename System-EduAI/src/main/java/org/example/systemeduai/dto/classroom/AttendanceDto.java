package org.example.systemeduai.dto.classroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.dto.student.StudentDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    private Integer classroomId;
    private String classroomName;
    private Long presentCount;
    private Long absentCount;
    private List<StudentDto> students;
}
