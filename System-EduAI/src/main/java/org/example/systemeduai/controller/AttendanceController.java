package org.example.systemeduai.controller;

import org.example.systemeduai.service.IAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    @Autowired
    private IAttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<?> recordAttendance(@RequestBody Map<String, Object> request) {
        try {
            Integer classId = (Integer) request.get("classId");
            String childName = (String) request.get("childName");
            String checkInTime = (String) request.get("checkInTime");
            String checkOutTime = (String) request.get("checkOutTime");
            String date = (String) request.get("date");

            Map<String, Object> result = attendanceService.recordAttendance(classId, childName, checkInTime, checkOutTime, date);
            return ResponseEntity.ok().body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error recording attendance: " + e.getMessage());
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getAttendanceByClassId(@PathVariable Integer classId, @RequestParam(required = false) String date) {
        try {
            Map<String, Object> result = attendanceService.getAttendanceByClassId(classId, date);
            return ResponseEntity.ok().body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving attendance: " + e.getMessage());
        }
    }
}
