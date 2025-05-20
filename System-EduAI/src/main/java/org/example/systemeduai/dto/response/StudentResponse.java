package org.example.systemeduai.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class StudentResponse {
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
    private Integer accountId;
    private Integer classroomId;
}
