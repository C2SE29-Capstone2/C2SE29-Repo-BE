package org.example.systemeduai.service;

import org.example.systemeduai.dto.internal.ClassroomTeacherInfo;
import org.example.systemeduai.dto.internal.CreateClassroomTeacherInfo;
import org.example.systemeduai.dto.request.CreateClassroomRequest;
import org.example.systemeduai.dto.request.UpdateClassroomRequest;
import org.example.systemeduai.dto.response.ModifyTeacherClassroomResponse;
import org.example.systemeduai.dto.response.ClassroomResponse;

import java.util.List;

public interface IClassroomService {
    List<ClassroomResponse> getAllClassrooms();
    void createClassroom(CreateClassroomRequest request);
    void updateClassroom(Integer classroomId, UpdateClassroomRequest request);
    void deleteClassroom(Integer classroomId);
    List<ModifyTeacherClassroomResponse> addTeachersToClassroom(Integer classroomId,
                                                                List<CreateClassroomTeacherInfo> teacherInfoList);
    List<ModifyTeacherClassroomResponse> removeTeachersFromClassroom(Integer classroomId, List<Integer> teacherIds);
    List<ClassroomResponse> getClassroomsByTeacherId(Integer teacherId);

}
