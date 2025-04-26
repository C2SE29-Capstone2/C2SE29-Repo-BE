package org.example.systemeduai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classroomId;
    private String classroomName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "classroom_type_id")
    private ClassroomType classroomType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "classroom_teachers",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private Set<Teacher> teachers = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "classroom_learning_schedules",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "learning_schedule_id"))
    private Set<LearningSchedule> learningSchedules = new LinkedHashSet<>();
}
