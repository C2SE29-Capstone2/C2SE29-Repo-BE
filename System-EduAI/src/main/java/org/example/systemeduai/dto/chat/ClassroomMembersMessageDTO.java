package org.example.systemeduai.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomMembersMessageDTO {
    private Integer classroomId;
    private String classroomName;
    private List<TeacherMessageDTO> teachers;
    private List<ParentMessageDTO> parents;
}
