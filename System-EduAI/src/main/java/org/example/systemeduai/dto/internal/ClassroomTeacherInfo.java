package org.example.systemeduai.dto.internal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassroomTeacherInfo {
    private Integer teacherId;
    private String teacherName;
    private String role;
}
