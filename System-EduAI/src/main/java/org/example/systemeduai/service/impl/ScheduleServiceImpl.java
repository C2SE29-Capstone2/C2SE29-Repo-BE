package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.schedule.ScheduleDTO;
import org.example.systemeduai.enums.ActivityType;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.ClassroomTeacher;
import org.example.systemeduai.model.Schedule;
import org.example.systemeduai.model.Teacher;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.repository.IClassroomTeacherRepository;
import org.example.systemeduai.repository.IScheduleRepository;
import org.example.systemeduai.repository.ITeacherRepository;
import org.example.systemeduai.service.IScheduleService;
import org.example.systemeduai.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements IScheduleService {

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private IClassroomRepository classroomRepository;

    @Autowired
    private ITeacherRepository teacherRepository;

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private IClassroomTeacherRepository classroomTeacherRepository;

    @Override
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDTO getScheduleById(Integer id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        if (schedule.isEmpty()) {
            throw new RuntimeException("Lịch trình không tìm thấy với ID: " + id);
        }
        return convertToDTO(schedule.get());
    }

    @Override
    public List<ScheduleDTO> getSchedulesByDate(Date date) {
        return scheduleRepository.findByTimeSlot(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getSchedulesByDateAndClassroom(Date date, Integer classroomId) {
        return scheduleRepository.findByTimeSlotAndClassroom_ClassroomId(date, classroomId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Integer loggedInTeacherId = getLoggedInTeacherId();

        checkTeacherPermission(loggedInTeacherId, scheduleDTO.getClassroomId());

        Schedule schedule = convertToEntity(scheduleDTO);
        schedule.setTeacher(teacherRepository.findById(loggedInTeacherId).orElseThrow(() ->
                new RuntimeException("Giáo viên không tìm thấy với ID: " + loggedInTeacherId)));
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return convertToDTO(savedSchedule);
    }

    @Override
    public ScheduleDTO updateSchedule(Integer id, ScheduleDTO scheduleDTO) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Lịch trình không tìm thấy với ID: " + id);
        }

        Integer loggedInTeacherId = getLoggedInTeacherId();

        checkTeacherPermission(loggedInTeacherId, scheduleDTO.getClassroomId());

        Schedule schedule = convertToEntity(scheduleDTO);
        schedule.setScheduleId(id);
        schedule.setTeacher(teacherRepository.findById(loggedInTeacherId).orElseThrow(() ->
                new RuntimeException("Giáo viên không tìm thấy với ID: " + loggedInTeacherId)));
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return convertToDTO(updatedSchedule);
    }

    @Override
    public void deleteSchedule(Integer id) {
        Optional<Schedule> scheduleOpt = scheduleRepository.findById(id);
        if (scheduleOpt.isEmpty()) {
            throw new RuntimeException("Lịch trình không tìm thấy với ID: " + id);
        }

        Schedule schedule = scheduleOpt.get();
        Integer loggedInTeacherId = getLoggedInTeacherId();

        checkTeacherPermission(loggedInTeacherId, schedule.getClassroom().getClassroomId());

        scheduleRepository.deleteById(id);
    }

    private ScheduleDTO convertToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setScheduleId(schedule.getScheduleId());
        dto.setName(schedule.getName());
        dto.setDate(schedule.getTimeSlot());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setActivityType(schedule.getActivityType());
        dto.setClassroomId(schedule.getClassroom() != null ? schedule.getClassroom().getClassroomId() : null);
        dto.setTeacherId(schedule.getTeacher() != null ? schedule.getTeacher().getTeacherId() : null);
        return dto;
    }

    private Integer getLoggedInTeacherId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Teacher teacher = teacherService.findTeacherByUsername(username);
        return teacher.getTeacherId();
    }

    private void checkTeacherPermission(Integer teacherId, Integer classroomId) {
        List<ClassroomTeacher> classroomTeachers = classroomTeacherRepository
                .findByTeacher_TeacherIdAndRole(teacherId, "MAIN");
        boolean hasPermission = classroomTeachers.stream()
                .anyMatch(ct -> ct.getClassroom().getClassroomId().equals(classroomId));
        if (!hasPermission) {
            throw new RuntimeException("Giáo viên không có quyền quản lý lớp học với ID: " + classroomId);
        }
    }

    private Schedule convertToEntity(ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setName(dto.getName());
        schedule.setTimeSlot(dto.getDate());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setActivityType(dto.getActivityType());

        if (dto.getClassroomId() != null) {
            Optional<Classroom> classroom = classroomRepository.findById(dto.getClassroomId());
            if (classroom.isEmpty()) {
                throw new RuntimeException("Lớp học không tìm thấy với ID: " + dto.getClassroomId());
            }
            schedule.setClassroom(classroom.get());
        }

        if (dto.getTeacherId() != null) {
            Optional<Teacher> teacher = teacherRepository.findById(dto.getTeacherId());
            if (teacher.isEmpty()) {
                throw new RuntimeException("Giáo viên không tìm thấy với ID: " + dto.getTeacherId());
            }
            schedule.setTeacher(teacher.get());
        }

        return schedule;
    }
}
