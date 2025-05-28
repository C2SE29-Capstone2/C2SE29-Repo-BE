package org.example.systemeduai.controller;

import org.example.systemeduai.dto.chat.ChatMessageDTO;
import org.example.systemeduai.dto.chat.ClassroomMembersMessageDTO;
import org.example.systemeduai.service.IChatMessageService;
import org.example.systemeduai.service.IClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private IClassroomService classroomService;

    @GetMapping("/members/{classroomId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ClassroomMembersMessageDTO> getClassroomMembers(@PathVariable Integer classroomId) {
        ClassroomMembersMessageDTO members = classroomService.getClassroomMembers(classroomId);
        return ResponseEntity.ok(members);
    }

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<ChatMessageDTO> sendMessage(
            @RequestParam Integer classroomId,
            @RequestParam Integer receiverId,
            @RequestParam boolean isTeacher,
            @RequestBody String content) {
        return ResponseEntity.ok(chatMessageService.sendMessage(classroomId, receiverId, isTeacher, content));
    }

    @MessageMapping("/send")
    public void sendMessageWebSocket(
            @Payload ChatMessageDTO messageDTO,
            @RequestParam Integer classroomId,
            @RequestParam Integer receiverId,
            @RequestParam boolean isTeacher) {
        chatMessageService.sendMessage(classroomId, receiverId, isTeacher, messageDTO.getContent());
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(
            @RequestParam Integer classroomId,
            @RequestParam Integer receiverId,
            @RequestParam boolean isTeacher) {
        return ResponseEntity.ok(chatMessageService.getChatHistory(classroomId, receiverId, isTeacher));
    }
}
