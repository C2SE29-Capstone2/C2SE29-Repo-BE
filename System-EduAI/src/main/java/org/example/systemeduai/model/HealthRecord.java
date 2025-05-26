package org.example.systemeduai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer healthRecordId;
    private LocalDate vaccinationSchedule;
    private String medicalHistory;
    private String periodicCheck;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private Student student;
}
