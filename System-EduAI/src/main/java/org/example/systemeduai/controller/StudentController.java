package org.example.systemeduai.controller;

import lombok.RequiredArgsConstructor;
import org.example.systemeduai.dto.request.CreateStudentRequest;
import org.example.systemeduai.dto.request.UpdateStudentRequest;
import org.example.systemeduai.dto.response.PageResponse;
import org.example.systemeduai.dto.response.StudentResponse;
import org.example.systemeduai.dto.student.StudentMeasurementUpdateDto;
import org.example.systemeduai.dto.student.StudentUpdateDto;
import org.example.systemeduai.dto.student.StudentUserDetailDto;
import org.example.systemeduai.model.Student;
import org.example.systemeduai.service.IStudentService;
import org.example.systemeduai.util.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final IStudentService studentService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<StudentResponse>> getStudents(
            @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstant.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstant.DEFAULT_SORT_DIR) String sortDir) {
        PageResponse<StudentResponse> students = studentService.getStudents(page, size, sortBy, sortDir);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/classroom/{classroomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<StudentResponse>> getStudentsByClassroom(
            @PathVariable Integer classroomId,
            @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstant.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstant.DEFAULT_SORT_DIR) String sortDir) {
        PageResponse<StudentResponse> students = studentService.getStudentsByClassroomId(classroomId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Integer studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }


//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> createStudent(@Valid @RequestBody CreateStudentRequest request) {
//        studentService.createStudent(request);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateStudent(@PathVariable Integer studentId,
                                              @Valid @RequestBody UpdateStudentRequest request) {
        studentService.updateStudent(request, studentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentUserDetailDto> getDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        StudentUserDetailDto studentUserDetailDto = studentService.findUserDetailByUsername(username);

        if (studentUserDetailDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(studentUserDetailDto, HttpStatus.OK);
    }

    @PutMapping("/detail")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentUserDetailDto> updateStudentDetails(@Valid @RequestBody StudentUpdateDto studentUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        StudentUserDetailDto updatedStudent = studentService.updateStudentDetails(username, studentUpdateDto);
        if (updatedStudent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    @PutMapping("/measurements")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentUserDetailDto> updateStudentMeasurements(@Valid @RequestBody StudentMeasurementUpdateDto measurementUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        StudentUserDetailDto updatedStudent = studentService.updateStudentMeasurements(username, measurementUpdateDto);
        if (updatedStudent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }
}
