package org.example.systemeduai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.systemeduai.enums.ActivityType;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalTime;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;
    private String name;
    private Date timeSlot;
    private LocalTime startTime;
    private LocalTime endTime;
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
