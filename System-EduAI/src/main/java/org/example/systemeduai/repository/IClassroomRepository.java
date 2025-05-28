package org.example.systemeduai.repository;

import org.example.systemeduai.dto.chat.ParentMessageDTO;
import org.example.systemeduai.dto.chat.TeacherMessageDTO;
import org.example.systemeduai.model.Classroom;
import org.example.systemeduai.model.ClassroomTeacher;
import org.example.systemeduai.model.Parent;
import org.example.systemeduai.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface IClassroomRepository extends JpaRepository<Classroom, Integer> {
    boolean existsByClassroomId(Integer classroomId);

    @Query(value = "SELECT t.teacher_id, t.teacher_name, t.teacher_email, t.teacher_phone, t.profile_image " +
            "FROM teacher t " +
            "INNER JOIN classroom_teachers ct ON t.teacher_id = ct.teacher_id " +
            "WHERE ct.classroom_id = :classroomId", nativeQuery = true)
    List<Object[]> findTeachersByClassroomId(@Param("classroomId") Integer classroomId);

    @Query(value = "SELECT p.parent_id, p.parent_name, s.student_id, s.profile_image " +
            "FROM parent p " +
            "INNER JOIN student s ON p.student_id = s.student_id " +
            "WHERE s.classroom_id = :classroomId", nativeQuery = true)
    List<Object[]> findParentsByClassroomId(@Param("classroomId") Integer classroomId);
}
