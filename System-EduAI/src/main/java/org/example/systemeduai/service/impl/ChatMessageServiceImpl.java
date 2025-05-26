package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.chat.ChatMessageDTO;
import org.example.systemeduai.model.*;
import org.example.systemeduai.repository.*;
import org.example.systemeduai.service.IChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageServiceImpl implements IChatMessageService {

    @Autowired
    private IChatMessageRepository chatMessageRepository;

    @Autowired
    private IClassroomRepository classroomRepository;

    @Autowired
    private ITeacherRepository teacherRepository;

    @Autowired
    private IClassroomTeacherRepository classroomTeacherRepository;

    @Autowired
    private IParentRepository parentRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public ChatMessageDTO sendMessage(Integer classroomId, Integer receiverId, boolean isTeacher, String content) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer senderId = getAccountIdFromUsername(username);

        boolean hasTeacherRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));
        boolean hasStudentRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"));

        if (isTeacher && !hasTeacherRole) {
            throw new IllegalStateException("User does not have ROLE_TEACHER");
        }
        if (!isTeacher && !hasStudentRole) {
            throw new IllegalStateException("User does not have ROLE_STUDENT (required for parent chat)");
        }

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with ID: " + classroomId));

        Teacher teacher = null;
        Parent parent = null;
        String senderName;
        String receiverName;
        String senderImage;
        String receiverImage;

        if (isTeacher) {
            teacher = teacherRepository.findByAccountId(senderId)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with account ID: " + senderId));
            if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacher.getTeacherId(), classroomId)) {
                throw new IllegalStateException("Teacher is not assigned to this classroom");
            }
            parent = parentRepository.findById(receiverId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent not found with ID: " + receiverId));
            Student student = studentRepository.findByParentParentId(parent.getParentId());
            if (student == null || !student.getClassroom().getClassroomId().equals(classroomId)) {
                throw new IllegalStateException("Parent's child is not in this classroom");
            }
            senderName = teacher.getTeacherName();
            receiverName = parent.getParentName();
            senderImage = teacher.getProfileImage();
            receiverImage = student.getProfileImage();
        } else {
            Student student = studentRepository.findByAccountId(senderId);
            if (student == null) {
                throw new IllegalStateException("Student not found for account ID: " + senderId);
            }
            parent = parentRepository.findByStudentId(student.getStudentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent not found for student ID: " + student.getStudentId()));
            if (!student.getClassroom().getClassroomId().equals(classroomId)) {
                throw new IllegalStateException("Parent's child is not in this classroom");
            }
            teacher = teacherRepository.findById(receiverId)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + receiverId));
            if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacher.getTeacherId(), classroomId)) {
                throw new IllegalStateException("Teacher is not assigned to this classroom");
            }
            senderName = parent.getParentName();
            receiverName = teacher.getTeacherName();
            senderImage = student.getProfileImage();
            receiverImage = teacher.getProfileImage();
        }

        ChatMessage message = new ChatMessage(content, LocalDateTime.now(), classroom, teacher, parent);
        message = chatMessageRepository.save(message);

        ChatMessageDTO messageDTO = convertToDTO(message, senderName, receiverName, senderImage, receiverImage);
        messagingTemplate.convertAndSend("/topic/private/" + teacher.getTeacherId() + "/" + parent.getParentId(), messageDTO);
        return messageDTO;
    }

    @Override
    public List<ChatMessageDTO> getChatHistory(Integer classroomId, Integer receiverId, boolean isTeacher) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer senderId = getAccountIdFromUsername(username);

        boolean hasTeacherRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));
        boolean hasStudentRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"));

        if (isTeacher && !hasTeacherRole) {
            throw new IllegalStateException("User does not have ROLE_TEACHER");
        }
        if (!isTeacher && !hasStudentRole) {
            throw new IllegalStateException("User does not have ROLE_STUDENT (required for parent chat)");
        }

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with ID: " + classroomId));

        Teacher teacher = null;
        Parent parent = null;
        Integer teacherId;
        Integer parentId;

        if (isTeacher) {
            teacher = teacherRepository.findByAccountId(senderId)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with account ID: " + senderId));
            if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacher.getTeacherId(), classroomId)) {
                throw new IllegalStateException("Teacher is not assigned to this classroom");
            }
            parent = parentRepository.findById(receiverId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent not found with ID: " + receiverId));
            Student student = studentRepository.findByParentParentId(parent.getParentId());
            if (student == null || !student.getClassroom().getClassroomId().equals(classroomId)) {
                throw new IllegalStateException("Parent's child is not in this classroom");
            }
            teacherId = teacher.getTeacherId();
            parentId = parent.getParentId();
        } else {
            Student student = studentRepository.findByAccountId(senderId);
            if (student == null) {
                throw new IllegalStateException("Student not found for account ID: " + senderId);
            }
            parent = parentRepository.findByStudentId(student.getStudentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent not found for student ID: " + student.getStudentId()));
            if (!student.getClassroom().getClassroomId().equals(classroomId)) {
                throw new IllegalStateException("Parent's child is not in this classroom");
            }
            teacher = teacherRepository.findById(receiverId)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + receiverId));
            if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacher.getTeacherId(), classroomId)) {
                throw new IllegalStateException("Teacher is not assigned to this classroom");
            }
            teacherId = teacher.getTeacherId();
            parentId = parent.getParentId();
        }

        return chatMessageRepository.findMessages(classroomId, teacherId, parentId)
                .stream()
                .map(message -> {
                    String senderName;
                    String receiverName;
                    String senderImage;
                    String receiverImage;

                    Student student = studentRepository.findByParentParentId(message.getParent().getParentId());
                    if (message.getTeacher().getTeacherId().equals(teacherId)) {
                        senderName = message.getTeacher().getTeacherName();
                        receiverName = message.getParent().getParentName();
                        senderImage = message.getTeacher().getProfileImage();
                        receiverImage = student != null ? student.getProfileImage() : null;
                    } else {
                        senderName = message.getParent().getParentName();
                        receiverName = message.getTeacher().getTeacherName();
                        senderImage = student != null ? student.getProfileImage() : null;
                        receiverImage = message.getTeacher().getProfileImage();
                    }

                    return convertToDTO(message, senderName, receiverName, senderImage, receiverImage);
                })
                .collect(Collectors.toList());
    }

    private Integer getAccountIdFromUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username))
                .getAccountId();
    }

    private ChatMessageDTO convertToDTO(ChatMessage message, String senderName, String receiverName, String senderImage, String receiverImage) {
        return new ChatMessageDTO(
                message.getMessageId(),
                message.getContent(),
                message.getTimestamp(),
                message.getClassroom().getClassroomId(),
                message.getTeacher().getTeacherId(),
                message.getParent().getParentId(),
                senderName,
                receiverName,
                senderImage,
                receiverImage
        );
    }
}