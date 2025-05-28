package org.example.systemeduai.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherMessageDTO {
    private Integer teacherId;
    private String teacherName;
    private String teacherEmail;
    private String teacherPhone;
    private String teacherImage;
}
