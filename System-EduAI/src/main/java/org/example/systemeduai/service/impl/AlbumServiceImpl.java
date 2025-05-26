package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.schedule.AlbumDTO;
import org.example.systemeduai.model.Album;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.ExtracurricularActivity;
import org.example.systemeduai.model.Teacher;
import org.example.systemeduai.repository.*;
import org.example.systemeduai.service.IAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements IAlbumService {

    @Autowired
    private IAlbumRepository albumRepository;

    @Autowired
    private IClassroomRepository classroomRepository;

    @Autowired
    private IExtracurricularActivityRepository activityRepository;

    @Autowired
    private IClassroomTeacherRepository classroomTeacherRepository;

    @Autowired
    private ITeacherRepository teacherRepository;

    @Override
    public AlbumDTO createAlbum(AlbumDTO dto, String username) {
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found for username: " + username));
        Integer teacherId = teacher.getTeacherId();

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with ID: " + dto.getClassroomId()));

        if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacherId, dto.getClassroomId())) {
            throw new SecurityException("You do not have permission to manage this classroom");
        }

        ExtracurricularActivity activity = activityRepository.findById(dto.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + dto.getActivityId()));

        if (!activity.getClassroom().getClassroomId().equals(dto.getClassroomId())) {
            throw new IllegalArgumentException("Activity does not belong to the specified classroom");
        }

        Album album = new Album();
        album.setAlbumName(dto.getAlbumName());
        album.setDescription(dto.getDescription());
        album.setCreatedDate(dto.getCreatedDate());
        album.setImageUrls(dto.getImageUrls());
        album.setClassroom(classroom);
        album.setActivity(activity);

        album = albumRepository.save(album);
        dto.setAlbumId(album.getAlbumId());
        return dto;
    }

    @Override
    public AlbumDTO getAlbumById(Integer id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with ID: " + id));
        return convertToDTO(album);
    }

    @Override
    public List<AlbumDTO> getAlbumsByClassroom(Integer classroomId) {
        return albumRepository.findByClassroomClassroomId(classroomId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlbumDTO> getAlbumsByActivity(Integer activityId) {
        return albumRepository.findByActivityActivityId(activityId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AlbumDTO updateAlbum(Integer id, AlbumDTO dto, String username) {
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found for username: " + username));
        Integer teacherId = teacher.getTeacherId();

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with ID: " + id));

        if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacherId, album.getClassroom().getClassroomId())) {
            throw new SecurityException("You do not have permission to manage this classroom");
        }

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found with ID: " + dto.getClassroomId()));

        if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacherId, dto.getClassroomId())) {
            throw new SecurityException("You do not have permission to manage the target classroom");
        }

        ExtracurricularActivity activity = activityRepository.findById(dto.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + dto.getActivityId()));

        if (!activity.getClassroom().getClassroomId().equals(dto.getClassroomId())) {
            throw new IllegalArgumentException("Activity does not belong to the specified classroom");
        }

        album.setAlbumName(dto.getAlbumName());
        album.setDescription(dto.getDescription());
        album.setCreatedDate(dto.getCreatedDate());
        album.setImageUrls(dto.getImageUrls());
        album.setClassroom(classroom);
        album.setActivity(activity);

        albumRepository.save(album);
        dto.setAlbumId(album.getAlbumId());
        return dto;
    }

    @Override
    public void deleteAlbum(Integer id, String username) {
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found for username: " + username));
        Integer teacherId = teacher.getTeacherId();

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with ID: " + id));

        if (!classroomTeacherRepository.existsByTeacherTeacherIdAndClassroomClassroomId(teacherId, album.getClassroom().getClassroomId())) {
            throw new SecurityException("You do not have permission to manage this classroom");
        }

        albumRepository.deleteById(id);
    }

    private AlbumDTO convertToDTO(Album album) {
        return new AlbumDTO(
                album.getAlbumId(),
                album.getAlbumName(),
                album.getDescription(),
                album.getCreatedDate(),
                album.getImageUrls(),
                album.getClassroom().getClassroomId(),
                album.getActivity().getActivityId()
        );
    }

}
