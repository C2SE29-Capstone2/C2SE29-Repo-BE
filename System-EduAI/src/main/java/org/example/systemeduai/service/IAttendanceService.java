package org.example.systemeduai.service;

import org.example.systemeduai.dto.attendance.ClassroomAttendanceSummaryDTO;
import org.example.systemeduai.dto.attendance.StudentAttendanceDTO;

import java.time.LocalDate;
import java.util.List;

public interface IAttendanceService {
    List<ClassroomAttendanceSummaryDTO> getClassroomAttendanceSummary(LocalDate date);

    List<StudentAttendanceDTO> getStudentAttendanceByClassroom(Integer classroomId, LocalDate date);
}
