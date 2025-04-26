package org.example.systemeduai.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teacherId;
    private String teacherName;
    private String teacherEmail;
    private String teacherPhone;
    private Boolean teacherGender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String teacherAddress;
    private String profileImage;
    private String qualifications;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @JsonBackReference
    @ManyToMany(mappedBy = "teachers")
    private Set<ContactBook> contactBooks = new LinkedHashSet<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "teachers")
    private Set<Classroom> classrooms = new LinkedHashSet<>();
}
