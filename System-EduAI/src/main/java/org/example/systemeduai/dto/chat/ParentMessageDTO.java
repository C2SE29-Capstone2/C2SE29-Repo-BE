package org.example.systemeduai.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentMessageDTO {
    private Integer parentId;
    private String parentName;
    private Integer studentId;
    private String parentImage;
}
