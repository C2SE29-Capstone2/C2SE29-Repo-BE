package org.example.systemeduai.dto.internal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateClassroomTeacherInfo {
    private Integer teacherId;
    private String role;
}
