package org.example.systemeduai.controller;

import lombok.RequiredArgsConstructor;
import org.example.systemeduai.dto.request.CreateTeacherRequest;
import org.example.systemeduai.dto.request.UpdateTeacherRequest;
import org.example.systemeduai.dto.response.PageResponse;
import org.example.systemeduai.dto.response.TeacherResponse;
import org.example.systemeduai.service.ITeacherService;
import org.example.systemeduai.util.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final ITeacherService teacherService;

    @GetMapping
    public ResponseEntity<PageResponse<TeacherResponse>> getTeachers(
            @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstant.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstant.DEFAULT_SORT_DIR) String sortDir) {
        return ResponseEntity.ok(teacherService.getTeachers(page, size, sortBy, sortDir));
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable Integer teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherById(teacherId));
    }

    @PostMapping
    public ResponseEntity<Void> createTeacher(@RequestBody CreateTeacherRequest request) {
        teacherService.createTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{teacherId}")
    public ResponseEntity<Void> updateTeacher(@PathVariable Integer teacherId,
                                              @RequestBody UpdateTeacherRequest request) {
        teacherService.updateTeacher(teacherId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Integer teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.noContent().build();
    }
}
