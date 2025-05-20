package org.example.systemeduai.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class TeacherResponse {
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
    private Integer accountId;
}
