package org.example.systemeduai.controller;

import org.example.systemeduai.service.IChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/children")
public class ChildrenController {

    @Autowired
    private IChildrenService childrenService;

    @PostMapping
    public ResponseEntity<?> addChild(@RequestBody Map<String, Object> request) {
        try {
            Integer classId = (Integer) request.get("classId");
            String name = (String) request.get("name");
            List<Double> embedding = (List<Double>) request.get("embedding");

            childrenService.addChild(classId, name, embedding);
            return ResponseEntity.ok().body(Map.of("message", "Child " + name + " added successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding child: " + e.getMessage());
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getChildrenByClassId(@PathVariable Integer classId) {
        try {
            Map<String, Object> result = childrenService.getChildrenByClassId(classId);
            return ResponseEntity.ok().body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving children: " + e.getMessage());
        }
    }
}
