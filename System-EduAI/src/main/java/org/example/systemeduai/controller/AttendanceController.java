package org.example.systemeduai.controller;

import org.example.systemeduai.dto.attendance.ClassroomAttendanceSummaryDTO;
import org.example.systemeduai.dto.attendance.StudentAttendanceDTO;
import org.example.systemeduai.service.IAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    @Autowired
    private IAttendanceService attendanceService;

    @GetMapping("/classrooms/summary")
    public ResponseEntity<List<ClassroomAttendanceSummaryDTO>> getClassroomAttendanceSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ClassroomAttendanceSummaryDTO> summaries = attendanceService.getClassroomAttendanceSummary(date);
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<StudentAttendanceDTO>> getStudentAttendanceByClassroom(
            @PathVariable Integer classroomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<StudentAttendanceDTO> studentAttendances = attendanceService.getStudentAttendanceByClassroom(classroomId, date);
        return ResponseEntity.ok(studentAttendances);
    }
}
