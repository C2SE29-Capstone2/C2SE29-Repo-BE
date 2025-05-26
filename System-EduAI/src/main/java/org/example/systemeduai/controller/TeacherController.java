package org.example.systemeduai.controller;

import org.example.systemeduai.dto.teacher.TeacherUpdateDto;
import org.example.systemeduai.dto.teacher.TeacherUserDetailDto;
import org.example.systemeduai.service.ITeacherService;
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
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;

    @GetMapping("/teacher/detail")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<TeacherUserDetailDto> getDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        TeacherUserDetailDto teacherUserDetailDto = teacherService.findUserDetailByUsername(username);

        if (teacherUserDetailDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(teacherUserDetailDto, HttpStatus.OK);
    }

    @PutMapping("/teacher/update")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<TeacherUserDetailDto> updateTeacher(@Valid @RequestBody TeacherUpdateDto updateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        TeacherUserDetailDto updatedTeacher = teacherService.updateTeacherDetails(username, updateDto);

        if (updatedTeacher == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedTeacher, HttpStatus.OK);
    }
}
