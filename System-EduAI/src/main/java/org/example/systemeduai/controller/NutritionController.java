package org.example.systemeduai.controller;

import org.example.systemeduai.dto.announcement.AnnouncementDTO;
import org.example.systemeduai.dto.nutrition.*;
import org.example.systemeduai.service.IAnnouncementService;
import org.example.systemeduai.service.INutritionLogService;
import org.example.systemeduai.service.INutritionMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NutritionController {

    @Autowired
    private INutritionMenuService nutritionMenuService;

    @Autowired
    private INutritionLogService nutritionLogService;

    @Autowired
    private IAnnouncementService announcementService;

    @GetMapping("/nutrition/menus")
    public ResponseEntity<List<NutritionMenuDTO>> getMealPlans(@RequestParam Integer classroomId,
                                                               @RequestParam String healthCondition) {
        List<NutritionMenuDTO> menus = nutritionMenuService.suggestMealPlan(classroomId, healthCondition);
        return ResponseEntity.ok(menus);
    }

    @PostMapping("/nutrition/logs")
    public ResponseEntity<NutritionLogDTO> createNutritionLog(@Valid @RequestBody CreateNutritionLogRequest request) {
        NutritionLogDTO log = nutritionLogService.trackMeal(request);
        return ResponseEntity.ok(log);
    }

    @PutMapping("/nutrition/logs/{logId}/completion")
    public ResponseEntity<NutritionLogDTO> updateNutritionLog(@PathVariable Integer logId,
                                                              @Valid @RequestBody UpdateNutritionLogRequest request) {
        NutritionLogDTO updatedLog = nutritionLogService.updateMealCompletion(logId, request);
        return ResponseEntity.ok(updatedLog);
    }

    @PostMapping("/nutrition/reminders")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AnnouncementDTO> sendMealReminder(@Valid @RequestBody CreateReminderRequest request) {
        AnnouncementDTO announcement = announcementService.sendMealReminder(request);
        return ResponseEntity.ok(announcement);
    }

    @GetMapping("/nutrition/logs")
    public ResponseEntity<List<NutritionLogDTO>> getNutritionLogs(@RequestParam Integer classroomId) {
        List<NutritionLogDTO> logs = nutritionLogService.getNutritionLogs(classroomId);
        return ResponseEntity.ok(logs);
    }
}
