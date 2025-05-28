package org.example.systemeduai.controller;

import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.service.IClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/classrooms")
public class ClassroomController {

    @Autowired
    private IClassroomService classroomService;

    @GetMapping("/{classId}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable Integer classId) {
        return classroomService.getClassroomById(classId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
