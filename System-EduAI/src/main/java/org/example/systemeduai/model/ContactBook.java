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
public class ContactBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactBookId;
    private String bloodGroup;
    private String bloodPressure;
    private Double height;
    private Double weight;
    private String allergies;
    private Integer totalAbsences;
    private String goodBehaviorCertificates;
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "contact_book_teachers",
            joinColumns = @JoinColumn(name = "contact_book_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private Set<Teacher> teachers = new LinkedHashSet<>();
}
