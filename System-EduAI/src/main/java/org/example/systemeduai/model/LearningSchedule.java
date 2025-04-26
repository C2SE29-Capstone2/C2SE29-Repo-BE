package org.example.systemeduai.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class LearningSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer learningScheduleId;
    private String name;
    private Date date;
    private String morning;
    private String noon;
    private String afternoon;

    @JsonBackReference
    @ManyToMany(mappedBy = "learningSchedules")
    private Set<Classroom> classrooms = new LinkedHashSet<>();
}
