package org.example.systemeduai.controller;

import org.example.systemeduai.dto.announcement.AnnouncementDTO;
import org.example.systemeduai.dto.health.GrowthRecordDTO;
import org.example.systemeduai.dto.health.HealthInfoDTO;
import org.example.systemeduai.service.IAnnouncementService;
import org.example.systemeduai.service.IGrowthRecordService;
import org.example.systemeduai.service.IHealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @Autowired
    private IHealthRecordService healthRecordService;

    @Autowired
    private IAnnouncementService announcementService;

    @Autowired
    private IGrowthRecordService growthRecordService;

    @PostMapping("/health-records")
    public ResponseEntity<HealthInfoDTO> createOrUpdateHealthRecord(@RequestBody HealthInfoDTO healthInfoDTO) {
        HealthInfoDTO savedHealthInfo = healthRecordService.saveHealthRecord(healthInfoDTO);
        return new ResponseEntity<>(savedHealthInfo, HttpStatus.CREATED);
    }

    @GetMapping("/health-records/{studentId}")
    public ResponseEntity<HealthInfoDTO> getHealthRecord(@PathVariable Integer studentId) {
        HealthInfoDTO healthInfo = healthRecordService.getHealthRecord(studentId);
        return new ResponseEntity<>(healthInfo, HttpStatus.OK);
    }

    @PostMapping("/health-reminders")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AnnouncementDTO> createHealthReminder(@RequestBody AnnouncementDTO announcementDTO,
                                                                @RequestParam Integer studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AnnouncementDTO createdAnnouncement = announcementService.createHealthReminder(announcementDTO, studentId, username);
        return new ResponseEntity<>(createdAnnouncement, HttpStatus.CREATED);
    }

    @PostMapping("/growth-records")
    public ResponseEntity<GrowthRecordDTO> createGrowthRecord(@RequestBody GrowthRecordDTO growthRecordDTO) {
        GrowthRecordDTO savedRecord = growthRecordService.saveGrowthRecord(growthRecordDTO);
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }

    @PutMapping("/growth-records/{growthRecordId}")
    public ResponseEntity<GrowthRecordDTO> updateGrowthRecord(@PathVariable Integer growthRecordId,
                                                              @RequestBody GrowthRecordDTO growthRecordDTO) {
        GrowthRecordDTO updatedRecord = growthRecordService.updateGrowthRecord(growthRecordId, growthRecordDTO);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    @GetMapping("/growth-records/{studentId}")
    public ResponseEntity<List<GrowthRecordDTO>> getGrowthRecords(@PathVariable Integer studentId) {
        List<GrowthRecordDTO> growthRecords = growthRecordService.getGrowthRecords(studentId);
        return new ResponseEntity<>(growthRecords, HttpStatus.OK);
    }

    @GetMapping("/growth-records/{studentId}/chart-data")
    public ResponseEntity<List<Map<String, Object>>> getGrowthChartData(@PathVariable Integer studentId) {
        List<Map<String, Object>> chartData = growthRecordService.getGrowthChartData(studentId);
        return new ResponseEntity<>(chartData, HttpStatus.OK);
    }
}
