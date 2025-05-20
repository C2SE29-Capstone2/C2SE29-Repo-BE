package org.example.systemeduai.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.validation.AgeConstraint;
import org.example.systemeduai.validation.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentRequest {
    @NotNull(message = "Student name cannot be null")
    @Min(value = 1, message = "Student name must be at least 1 character long")
    @Max(value = 50, message = "Student name must be less than or equal to 50 characters long")
    private String studentName;
    @NotNull(message = "Student email cannot be null")
    @Email
    private String studentEmail;
    @Length(min = 10, max = 10, message = "Phone number must be 10 characters long")
    private String studentPhone;
    private Boolean studentGender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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
