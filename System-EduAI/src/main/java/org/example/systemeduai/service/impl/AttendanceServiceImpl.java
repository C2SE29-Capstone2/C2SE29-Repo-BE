package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.attendance.ClassroomAttendanceSummaryDTO;
import org.example.systemeduai.dto.attendance.StudentAttendanceDTO;
import org.example.systemeduai.model.Attendance;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.Student;
import org.example.systemeduai.repository.IAttendanceRepository;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.repository.IStudentRepository;
import org.example.systemeduai.service.IAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements IAttendanceService {

    @Autowired
    private IAttendanceRepository attendanceRepository;

    @Autowired
    private IClassroomRepository classroomRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Override
    public List<ClassroomAttendanceSummaryDTO> getClassroomAttendanceSummary(LocalDate date) {
        List<Classroom> classrooms = classroomRepository.findAll();
        List<ClassroomAttendanceSummaryDTO> summaries = new ArrayList<>();

        for (Classroom classroom : classrooms) {
            List<Student> students = studentRepository.findAll().stream()
                    .filter(student -> student.getClassroom() != null && student.getClassroom().getClassroomId().equals(classroom.getClassroomId()))
                    .collect(Collectors.toList());

            List<Attendance> attendances = attendanceRepository.findAll().stream()
                    .filter(att -> att.getClassroom().getClassroomId().equals(classroom.getClassroomId()) && att.getDate().equals(date))
                    .collect(Collectors.toList());

            int totalStudents = students.size();
            int presentStudents = (int) attendances.stream().filter(att -> att.getCheckInTime() != null).count();
            int absentStudents = totalStudents - presentStudents;

            ClassroomAttendanceSummaryDTO summary = new ClassroomAttendanceSummaryDTO();
            summary.setClassroomId(classroom.getClassroomId());
            summary.setClassroomName(classroom.getClassroomName());
            summary.setClassroomType(classroom.getClassroomType().toString());
            summary.setTotalStudents(totalStudents);
            summary.setPresentStudents(presentStudents);
            summary.setAbsentStudents(absentStudents);

            summaries.add(summary);
        }

        return summaries;
    }

    @Override
    public List<StudentAttendanceDTO> getStudentAttendanceByClassroom(Integer classroomId, LocalDate date) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        List<Student> students = studentRepository.findAll().stream()
                .filter(student -> student.getClassroom() != null && student.getClassroom().getClassroomId().equals(classroomId))
                .collect(Collectors.toList());

        List<Attendance> attendances = attendanceRepository.findAll().stream()
                .filter(att -> att.getClassroom().getClassroomId().equals(classroomId) && att.getDate().equals(date))
                .collect(Collectors.toList());

        List<StudentAttendanceDTO> studentAttendanceList = new ArrayList<>();

        for (Student student : students) {
            StudentAttendanceDTO dto = new StudentAttendanceDTO();
            dto.setStudentId(student.getStudentId());
            dto.setStudentName(student.getStudentName());
            dto.setProfileImage(student.getProfileImage());
            dto.setClassroomName(classroom.getClassroomName());

            Attendance attendance = attendances.stream()
                    .filter(att -> att.getChildName().equals(student.getStudentName()))
                    .findFirst()
                    .orElse(null);

            if (attendance != null && attendance.getCheckInTime() != null) {
                dto.setAttendanceStatus("PRESENT");
                dto.setCheckInTime(attendance.getCheckInTime());
                dto.setCheckOutTime(attendance.getCheckOutTime());
            } else {
                dto.setAttendanceStatus("ABSENT");
                dto.setCheckInTime(null);
                dto.setCheckOutTime(null);
            }

            studentAttendanceList.add(dto);
        }

        return studentAttendanceList;
    }
}
