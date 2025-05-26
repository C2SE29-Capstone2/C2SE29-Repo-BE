package org.example.systemeduai.controller;

import org.example.systemeduai.dto.schedule.AlbumDTO;
import org.example.systemeduai.service.IAlbumService;
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
public class AlbumController {

    @Autowired
    private IAlbumService albumService;

    @PostMapping("/albums")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AlbumDTO> createAlbum(@Valid @RequestBody AlbumDTO dto, Principal principal) {
        return new ResponseEntity<>(albumService.createAlbum(dto, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Integer id) {
        return ResponseEntity.ok(albumService.getAlbumById(id));
    }

    @GetMapping("/albums/classroom/{classroomId}")
    public ResponseEntity<List<AlbumDTO>> getAlbumsByClassroom(@PathVariable Integer classroomId) {
        return ResponseEntity.ok(albumService.getAlbumsByClassroom(classroomId));
    }

    @GetMapping("/albums/activity/{activityId}")
    public ResponseEntity<List<AlbumDTO>> getAlbumsByActivity(@PathVariable Integer activityId) {
        return ResponseEntity.ok(albumService.getAlbumsByActivity(activityId));
    }

    @PutMapping("/albums/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AlbumDTO> updateAlbum(@PathVariable Integer id,
                                                @Valid @RequestBody AlbumDTO dto,
                                                Principal principal) {
        return ResponseEntity.ok(albumService.updateAlbum(id, dto, principal.getName()));
    }

    @DeleteMapping("/albums/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Integer id, Principal principal) {
        albumService.deleteAlbum(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
