package org.example.systemeduai.service;

import org.example.systemeduai.dto.chat.ChatMessageDTO;

import java.util.List;

public interface IChatMessageService {
    ChatMessageDTO sendMessage(Integer classroomId, Integer receiverId, boolean isTeacher, String content);

    List<ChatMessageDTO> getChatHistory(Integer classroomId, Integer receiverId, boolean isTeacher);
}
