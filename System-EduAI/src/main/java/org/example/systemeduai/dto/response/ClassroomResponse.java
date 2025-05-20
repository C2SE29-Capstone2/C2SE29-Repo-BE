package org.example.systemeduai.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.systemeduai.dto.internal.ClassroomTeacherInfo;

import java.util.List;

@Data
@Builder
public class ClassroomResponse {
    private Integer classroomId;
    private String classroomName;
    private String classroomType;
    private List<ClassroomTeacherInfo> classroomTeachers;
}
