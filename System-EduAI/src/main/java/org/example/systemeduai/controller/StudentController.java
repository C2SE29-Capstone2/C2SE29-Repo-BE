package org.example.systemeduai.controller;

import org.example.systemeduai.dto.student.StudentMeasurementUpdateDto;
import org.example.systemeduai.dto.student.StudentUpdateDto;
import org.example.systemeduai.dto.student.StudentUserDetailDto;
import org.example.systemeduai.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @GetMapping("/student/detail")
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

    @PutMapping("/student/detail")
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

    @PutMapping("/student/measurements")
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
