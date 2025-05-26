package org.example.systemeduai.controller;

import org.example.systemeduai.dto.schedule.ExtracurricularActivityDTO;
import org.example.systemeduai.service.IExtracurricularActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExtracurricularActivityController {

    @Autowired
    private IExtracurricularActivityService activityService;

    @PostMapping("/activities")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExtracurricularActivityDTO> createActivity(@Valid @RequestBody ExtracurricularActivityDTO dto,
                                                                     Principal principal) {
        return new ResponseEntity<>(activityService.createActivity(dto, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/activities/{id}")
    public ResponseEntity<ExtracurricularActivityDTO> getActivityById(@PathVariable Integer id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @GetMapping("/activities/classroom/{classroomId}")
    public ResponseEntity<List<ExtracurricularActivityDTO>> getActivitiesByClassroom(@PathVariable Integer classroomId) {
        return ResponseEntity.ok(activityService.getActivitiesByClassroom(classroomId));
    }

    @PutMapping("/activities/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExtracurricularActivityDTO> updateActivity(@PathVariable Integer id,
                                                                     @Valid @RequestBody ExtracurricularActivityDTO dto,
                                                                     Principal principal) {
        return ResponseEntity.ok(activityService.updateActivity(id, dto, principal.getName()));
    }

    @DeleteMapping("/activities/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteActivity(@PathVariable Integer id, Principal principal) {
        activityService.deleteActivity(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
