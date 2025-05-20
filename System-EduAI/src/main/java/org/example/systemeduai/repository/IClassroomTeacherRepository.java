package org.example.systemeduai.repository;

import org.example.systemeduai.model.ClassroomTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional
public interface IClassroomTeacherRepository extends JpaRepository<ClassroomTeacher, Integer> {
    List<ClassroomTeacher> findByClassroomClassroomId(Integer classroomId);
    List<ClassroomTeacher> findByClassroomClassroomIdIn(List<Integer> classroomIds);
    List<ClassroomTeacher> findByTeacherTeacherId(Integer teacherId);
    List<ClassroomTeacher> findByTeacherTeacherIdIn(List<Integer> teacherIds);
    @Query("SELECT DISTINCT ct.teacher.teacherId FROM ClassroomTeacher ct WHERE ct.teacher.teacherId IN :teacherIds" +
            " AND ct.classroom.classroomId = :classroomId")
    Set<Integer> findDistinctTeacherIdsByTeacherIdAndClassroomId(
            @Param("teacherIds") List<Integer> teacherIds,
            @Param("classroomId") Integer classroomId
    );
    @Modifying
    void deleteByTeacherTeacherId(Integer teacherId);
    @Modifying
    void deleteByClassroomClassroomId(Integer classroomId);
    @Modifying
    void deleteByClassroomClassroomIdAndTeacherTeacherId(Integer classroomId, Integer teacherId);
}
