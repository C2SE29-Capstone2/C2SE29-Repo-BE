package org.example.systemeduai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.dto.internal.CreateClassroomTeacherInfo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClassroomRequest {
    private String classroomName;
    private String classroomType;
    private List<CreateClassroomTeacherInfo> teacherInfoList;
}
