package org.example.systemeduai.service;

import org.example.systemeduai.dto.schedule.ExtracurricularActivityDTO;

import java.util.List;

public interface IExtracurricularActivityService {
    ExtracurricularActivityDTO createActivity(ExtracurricularActivityDTO dto, String username);

    ExtracurricularActivityDTO getActivityById(Integer id);

    List<ExtracurricularActivityDTO> getActivitiesByClassroom(Integer classroomId);

    ExtracurricularActivityDTO updateActivity(Integer id, ExtracurricularActivityDTO dto, String username);

    void deleteActivity(Integer id, String username);
}
