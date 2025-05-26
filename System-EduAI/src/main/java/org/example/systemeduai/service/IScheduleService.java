package org.example.systemeduai.service;

import org.example.systemeduai.dto.schedule.ScheduleDTO;
import org.example.systemeduai.enums.ActivityType;
import org.example.systemeduai.model.Schedule;

import java.sql.Date;
import java.util.List;

public interface IScheduleService {

    List<ScheduleDTO> getAllSchedules();

    ScheduleDTO getScheduleById(Integer id);

    List<ScheduleDTO> getSchedulesByDate(Date date);

    List<ScheduleDTO> getSchedulesByDateAndClassroom(Date date, Integer classroomId);

    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);

    ScheduleDTO updateSchedule(Integer id, ScheduleDTO scheduleDTO);

    void deleteSchedule(Integer id);
}
