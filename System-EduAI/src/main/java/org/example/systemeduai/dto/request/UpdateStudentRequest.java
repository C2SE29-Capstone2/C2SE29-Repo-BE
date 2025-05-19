package org.example.systemeduai.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.validation.AgeConstraint;
import org.example.systemeduai.validation.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRequest {
    private String studentName;
    @Email
    private String studentEmail;
    @Length(min = 10, max = 10, message = "Phone number must be 10 characters long")
    private String studentPhone;
    private Boolean studentGender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @AgeConstraint
    private Date dateOfBirth;
    @Min(value = 3, message = "Age must be greater than or equal to 3")
    @Max(value = 6, message = "Age must be less than or equal to 6")
    private Integer age;
    private String studentAddress;
    private String profileImage;
    private String healthStatus;
    private String hobby;
    private Integer classroomId;
}
