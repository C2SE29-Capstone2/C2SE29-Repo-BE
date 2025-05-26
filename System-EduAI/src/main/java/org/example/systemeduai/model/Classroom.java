package org.example.systemeduai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.systemeduai.enums.ClassroomType;

import javax.persistence.*;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classroomId;
    private String classroomName;

    @Enumerated(EnumType.STRING)
    @Column(name = "classroom_type")
    private ClassroomType classroomType;
}
