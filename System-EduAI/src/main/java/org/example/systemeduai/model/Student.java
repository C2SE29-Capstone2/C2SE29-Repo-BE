package org.example.systemeduai.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private Boolean studentGender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private Integer age;
    private String studentAddress;
    private String profileImage;
    private String healthStatus;
    private String hobby;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    public Student(String studentName, String studentEmail, String studentPhone, Boolean studentGender,
                   Date dateOfBirth, String studentAddress, Account account) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentPhone = studentPhone;
        this.studentGender = studentGender;
        this.dateOfBirth = dateOfBirth;
        this.studentAddress = studentAddress;
        this.account = account;
    }
}
