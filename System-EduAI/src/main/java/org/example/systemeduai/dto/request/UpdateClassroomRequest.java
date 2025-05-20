package org.example.systemeduai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClassroomRequest {
    private String classroomName;
    private String classroomType;
}
