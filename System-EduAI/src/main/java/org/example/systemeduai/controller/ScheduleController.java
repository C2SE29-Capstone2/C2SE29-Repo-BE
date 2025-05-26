package org.example.systemeduai.controller;

import org.example.systemeduai.dto.schedule.ExtracurricularActivityDTO;
import org.example.systemeduai.dto.schedule.ScheduleDTO;
import org.example.systemeduai.model.ExtracurricularActivity;
import org.example.systemeduai.service.IExtracurricularActivityService;
import org.example.systemeduai.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ScheduleController {

    @Autowired
    private IScheduleService scheduleService;

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Integer id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @GetMapping("/schedules/by-date")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByDate(@RequestParam("date") String date) {
        Date sqlDate = Date.valueOf(date);
        return ResponseEntity.ok(scheduleService.getSchedulesByDate(sqlDate));
    }

    @GetMapping("/schedules/by-date-and-classroom")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByDateAndClassroom(@RequestParam("date") String date,
                                                                            @RequestParam("classroomId") Integer classroomId) {
        Date sqlDate = Date.valueOf(date);
        return ResponseEntity.ok(scheduleService.getSchedulesByDateAndClassroom(sqlDate, classroomId));
    }

    @PostMapping("/schedules")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.status(201).body(scheduleService.createSchedule(scheduleDTO));
    }

    @PutMapping("/schedules/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable Integer id, @RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, scheduleDTO));
    }

    @DeleteMapping("/schedules/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Integer id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
