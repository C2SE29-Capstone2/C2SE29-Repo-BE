package org.example.systemeduai.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Integer messageId;
    private String content;
    private LocalDateTime timestamp;
    private Integer classroomId;
    private Integer teacherId;
    private Integer parentId;
    private String senderName;
    private String receiverName;
    private String senderImage;
    private String receiverImage;
}