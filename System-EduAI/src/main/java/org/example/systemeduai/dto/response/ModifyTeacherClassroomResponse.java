package org.example.systemeduai.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyTeacherClassroomResponse {
    private Integer teacherId;
    private String teacherName;
    private boolean isAdded;
}
