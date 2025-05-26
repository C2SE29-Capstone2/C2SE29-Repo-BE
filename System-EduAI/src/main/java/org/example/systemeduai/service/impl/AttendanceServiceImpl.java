package org.example.systemeduai.service.impl;

import org.example.systemeduai.model.Attendance;
import org.example.systemeduai.repository.IAttendanceRepository;
import org.example.systemeduai.repository.IClassroomRepository;
import org.example.systemeduai.service.IAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements IAttendanceService {

    @Autowired
    private IAttendanceRepository attendanceRepository;

    @Autowired
    private IClassroomRepository classroomRepository;

    @Override
    public Map<String, Object> recordAttendance(Integer classId, String childName, String checkInTime, String checkOutTime, String date) {
        if (!classroomRepository.existsByClassroomId(classId)) {
            throw new IllegalArgumentException("Classroom with ID " + classId + " does not exist");
        }

        LocalDate localDate = LocalDate.parse(date);
        Attendance existing = attendanceRepository.findByClassroomClassroomIdAndChildNameAndDate(classId, childName, localDate);

        if (existing != null) {
            if (checkInTime != null && existing.getCheckInTime() != null) {
                return Map.of("status", "already_checked_in", "time", existing.getCheckInTime().toString());
            } else if (checkOutTime != null && existing.getCheckOutTime() != null) {
                return Map.of("status", "already_checked_out", "time", existing.getCheckOutTime().toString());
            }
        } else {
            existing = new Attendance();
            existing.setChildName(childName);
            existing.setDate(localDate);
            existing.setClassroom(classroomRepository.findById(classId).orElse(null));
        }

        if (checkInTime != null) {
            existing.setCheckInTime(LocalDateTime.parse(checkInTime));
        } else if (checkOutTime != null) {
            existing.setCheckOutTime(LocalDateTime.parse(checkOutTime));
        }

        attendanceRepository.save(existing);
        String status = checkInTime != null ? "check_in" : "check_out";
        String time = checkInTime != null ? checkInTime : checkOutTime;
        return Map.of("status", status, "time", time);
    }

    @Override
    public Map<String, Object> getAttendanceByClassId(Integer classId, String date) {
        if (!classroomRepository.existsByClassroomId(classId)) {
            throw new IllegalArgumentException("Classroom with ID " + classId + " does not exist");
        }

        List<Attendance> attendanceRecords;
        if (date != null) {
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(date);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
            }
            attendanceRecords = attendanceRepository.findByClassroomClassroomIdAndDate(classId, localDate);
        } else {
            attendanceRecords = attendanceRepository.findByClassroomClassroomId(classId);
        }

        List<Map<String, Object>> records = attendanceRecords.stream().map(record -> Map.of(
                "child_name", (Object) record.getChildName(),
                "check_in_time", (Object) (record.getCheckInTime() != null ? record.getCheckInTime().toString() : null),
                "check_out_time", (Object) (record.getCheckOutTime() != null ? record.getCheckOutTime().toString() : null),
                "date", (Object) record.getDate().toString()
        )).collect(Collectors.toList());

        return Map.of(
                "class_id", classId,
                "attendance_records", records,
                "total_records", records.size()
        );
    }
}
