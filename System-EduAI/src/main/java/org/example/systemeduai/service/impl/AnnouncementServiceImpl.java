package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.announcement.AnnouncementDTO;
import org.example.systemeduai.dto.nutrition.CreateReminderRequest;
import org.example.systemeduai.model.Announcement;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.Teacher;
import org.example.systemeduai.repository.IAnnouncementRepository;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.repository.IStudentRepository;
import org.example.systemeduai.repository.ITeacherRepository;
import org.example.systemeduai.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AnnouncementServiceImpl implements IAnnouncementService {
    @Autowired
    private IClassroomRepository classroomRepository;

    @Autowired
    private IAnnouncementRepository announcementRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private ITeacherRepository teacherRepository;

    @Override
    public AnnouncementDTO sendMealReminder(CreateReminderRequest request) {

        if (request == null || request.getClassroomId() == null || request.getMealTime() == null ||
                request.getMealDetails() == null || request.getMealDetails().trim().isEmpty()) {
            throw new IllegalArgumentException("Classroom ID, meal time, and meal details must not be null or empty");
        }

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with username: " + username));

        Classroom classroom = classroomRepository.findById(request.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + request.getClassroomId()));

        String mealTimeDisplay;
        switch (request.getMealTime()) {
            case BREAKFAST:
                mealTimeDisplay = "bữa sáng";
                break;
            case LUNCH:
                mealTimeDisplay = "bữa trưa";
                break;
            case AFTERNOON_SNACK:
                mealTimeDisplay = "bữa chiều nhẹ";
                break;
            default:
                mealTimeDisplay = request.getMealTime().toString();
        }

        Announcement announcement = new Announcement();
        announcement.setTitle("Nhắc nhở " + mealTimeDisplay + " cho lớp " + classroom.getClassroomName());
        announcement.setContent("Hôm nay, " + LocalDate.now() + ", các bé ở lớp " + classroom.getClassroomName() +
                " sẽ có " + mealTimeDisplay + ": " + request.getMealDetails());
        announcement.setSentDate(LocalDate.now());
        announcement.setTeacher(teacher);

        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return convertToAnnouncementDTO(savedAnnouncement);
    }

    private AnnouncementDTO convertToAnnouncementDTO(Announcement announcement) {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setAnnouncementId(announcement.getAnnouncementId());
        dto.setTitle(announcement.getTitle());
        dto.setContent(announcement.getContent());
        dto.setSentDate(announcement.getSentDate());
        dto.setTeacherId(announcement.getTeacher().getTeacherId());
        return dto;
    }

    @Override
    public AnnouncementDTO createHealthReminder(AnnouncementDTO announcementDTO, Integer studentId,
                                                String authenticatedUsername) {
        if (announcementDTO.getTitle() == null || announcementDTO.getContent() == null) {
            throw new IllegalArgumentException("Announcement title and content are required");
        }
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID is required");
        }
        if (authenticatedUsername == null) {
            throw new IllegalArgumentException("Authenticated username must be provided");
        }

        Teacher authenticatedTeacher = teacherRepository.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new SecurityException("Authenticated teacher not found for username: " + authenticatedUsername));

        studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        Announcement announcement = new Announcement();
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setContent(announcementDTO.getContent());
        announcement.setSentDate(LocalDate.now());
        announcement.setTeacher(authenticatedTeacher);

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        announcementDTO.setAnnouncementId(savedAnnouncement.getAnnouncementId());
        announcementDTO.setSentDate(savedAnnouncement.getSentDate());
        announcementDTO.setTeacherId(authenticatedTeacher.getTeacherId());
        return announcementDTO;
    }


}
