package org.example.systemeduai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.systemeduai.validation.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeacherRequest {
    @NotNull(message = "Teacher name cannot be null")
    @Min(value = 1, message = "Teacher name must be at least 1 character long")
    @Length(max = 50, message = "Teacher name must be less than or equal to 50 characters long")
    private String teacherName;
    @NotNull(message = "Teacher email cannot be null")
    @Email
    private String teacherEmail;
    @Length(min = 10, max = 10, message = "Phone number must be 10 characters")
    private String teacherPhone;
    private Boolean teacherGender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateOfBirth;
    private String teacherAddress;
    private String profileImage;
    private String qualifications;
}
