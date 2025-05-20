package org.example.systemeduai.model;

import lombok.*;
import org.example.systemeduai.enums.ClassroomType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classroomId;
    private String classroomName;
    @Enumerated(EnumType.STRING)
    @Column(name = "classroom_type")
    private ClassroomType classroomType;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "classroom_learning_schedules",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "learning_schedule_id"))
    private Set<LearningSchedule> learningSchedules = new HashSet<>();
}
