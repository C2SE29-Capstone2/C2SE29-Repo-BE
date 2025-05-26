package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.schedule.ExtracurricularActivityDTO;
import org.example.systemeduai.model.Announcement;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.ExtracurricularActivity;
import org.example.systemeduai.model.Teacher;
import org.example.systemeduai.repository.*;
import org.example.systemeduai.service.IExtracurricularActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtracurricularActivityServiceImpl implements IExtracurricularActivityService {

    @Autowired
    private IExtracurricularActivityRepository activityRepository;

    @Autowired
    private IClassroomRepository classroomRepository;

    @Autowired
    private ITeacherRepository teacherRepository;

    @Autowired
    private IClassroomTeacherRepository classroomTeacherRepository;

    @Autowired
    private IAlbumRepository albumRepository;

    @Autowired
    private IAnnouncementRepository announcementRepository;

    @Override
    public ExtracurricularActivityDTO createActivity(ExtracurricularActivityDTO dto, String username) {

        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found for username: " + username));
        Integer teacherId = teacher.getTeacherId();

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with ID: " + dto.getClassroomId()));

        if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacherId, dto.getClassroomId())) {
            throw new SecurityException("You do not have permission to manage this classroom");
        }

        ExtracurricularActivity activity = new ExtracurricularActivity();
        activity.setName(dto.getName());
        activity.setDescription(dto.getDescription());
        activity.setActivityDate(dto.getActivityDate());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setActivityType(dto.getActivityType());
        activity.setClassroom(classroom);
        activity.setTeacher(teacher);

        activity = activityRepository.save(activity);
        dto.setActivityId(activity.getActivityId());
        dto.setTeacherId(teacherId);

        Announcement announcement = new Announcement();
        announcement.setTitle("Thông Báo Hoạt Động: " + dto.getName());
        announcement.setContent("Hoạt động '" + dto.getName() + "' sẽ diễn ra vào ngày " + dto.getActivityDate() +
                " từ " + dto.getStartTime() + " đến " + dto.getEndTime() + ".");
        announcement.setSentDate(LocalDate.now());
        announcement.setTeacher(teacher);
        announcementRepository.save(announcement);

        return dto;
    }

    @Override
    public ExtracurricularActivityDTO getActivityById(Integer id) {
        ExtracurricularActivity activity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));
        return convertToDTO(activity);
    }

    @Override
    public List<ExtracurricularActivityDTO> getActivitiesByClassroom(Integer classroomId) {
        return activityRepository.findByClassroomClassroomId(classroomId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExtracurricularActivityDTO updateActivity(Integer id, ExtracurricularActivityDTO dto, String username) {
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found for username: " + username));
        Integer teacherId = teacher.getTeacherId();

        ExtracurricularActivity activity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));

        if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacherId, activity.getClassroom().getClassroomId())) {
            throw new SecurityException("You do not have permission to manage this classroom");
        }

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with ID: " + dto.getClassroomId()));

        if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacherId, dto.getClassroomId())) {
            throw new SecurityException("You do not have permission to manage the target classroom");
        }

        activity.setName(dto.getName());
        activity.setDescription(dto.getDescription());
        activity.setActivityDate(dto.getActivityDate());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setActivityType(dto.getActivityType());
        activity.setClassroom(classroom);

        activityRepository.save(activity);
        dto.setActivityId(activity.getActivityId());
        dto.setTeacherId(teacherId);
        return dto;
    }

    @Override
    public void deleteActivity(Integer id, String username) {
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found for username: " + username));
        Integer teacherId = teacher.getTeacherId();

        ExtracurricularActivity activity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));

        if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacherId, activity.getClassroom().getClassroomId())) {
            throw new SecurityException("You do not have permission to manage this classroom");
        }

        albumRepository.deleteByActivityActivityId(id);

        activityRepository.deleteById(id);
    }

    private ExtracurricularActivityDTO convertToDTO(ExtracurricularActivity activity) {
        return new ExtracurricularActivityDTO(
                activity.getActivityId(),
                activity.getName(),
                activity.getDescription(),
                activity.getActivityDate(),
                activity.getStartTime(),
                activity.getEndTime(),
                activity.getActivityType(),
                activity.getClassroom().getClassroomId(),
                activity.getTeacher().getTeacherId()
        );
    }
}
