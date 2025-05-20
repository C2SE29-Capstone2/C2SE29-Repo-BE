package org.example.systemeduai.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassroomTeacherResponse {
    private Integer classroomId;
    private String classroomName;
    private String classroomType;
    private String teacherName;
    private String role;
}
