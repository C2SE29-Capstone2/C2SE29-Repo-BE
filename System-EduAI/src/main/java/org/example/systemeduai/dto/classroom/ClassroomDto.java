package org.example.systemeduai.dto.classroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomDto {
    private Integer classroomId;
    private String classroomName;
    private Long presentCount;
    private Long totalCapacity;
    private String teacherName;
}
