package org.example.systemeduai.controller;

import lombok.RequiredArgsConstructor;
import org.example.systemeduai.dto.internal.ClassroomTeacherInfo;
import org.example.systemeduai.dto.internal.CreateClassroomTeacherInfo;
import org.example.systemeduai.dto.request.CreateClassroomRequest;
import org.example.systemeduai.dto.request.UpdateClassroomRequest;
import org.example.systemeduai.dto.response.ClassroomResponse;
import org.example.systemeduai.dto.response.ModifyTeacherClassroomResponse;
import org.example.systemeduai.service.IClassroomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classrooms")
@RequiredArgsConstructor
public class ClassroomController {
    private final IClassroomService classroomService;

    @GetMapping
    public ResponseEntity<List<ClassroomResponse>> getAllClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createClassroom(@RequestBody CreateClassroomRequest request) {
        classroomService.createClassroom(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{classroomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateClassroom(@PathVariable Integer classroomId,
                                               @RequestBody UpdateClassroomRequest request) {
        classroomService.updateClassroom(classroomId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{classroomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Integer classroomId) {
        classroomService.deleteClassroom(classroomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/teachers/{teacherId}")
    public ResponseEntity<List<ClassroomResponse>> getClassroomsByTeacherId(
            @PathVariable Integer teacherId) {
        return ResponseEntity.ok(classroomService.getClassroomsByTeacherId(teacherId));
    }

    @PostMapping("/{classroomId}/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ModifyTeacherClassroomResponse>> addTeachersToClassroom(
            @PathVariable Integer classroomId,
            @RequestBody List<CreateClassroomTeacherInfo> teacherInfoList) {
        return ResponseEntity.ok(classroomService.addTeachersToClassroom(classroomId, teacherInfoList));
    }

    @DeleteMapping("/{classroomId}/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ModifyTeacherClassroomResponse>> removeTeachersFromClassroom(
            @PathVariable Integer classroomId,
            @RequestBody List<Integer> teacherIds) {
        return ResponseEntity.ok(classroomService.removeTeachersFromClassroom(classroomId, teacherIds));
    }
} 