package org.example.systemeduai.repository;

import org.example.systemeduai.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface IChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    @Query("SELECT m FROM ChatMessage m WHERE m.classroom.classroomId = :classroomId " +
            "AND ((m.teacher.teacherId = :teacherId AND m.parent.parentId = :parentId) " +
            "OR (m.teacher.teacherId = :parentId AND m.parent.parentId = :teacherId))")
    List<ChatMessage> findMessages(@Param("classroomId") Integer classroomId,
                                   @Param("teacherId") Integer teacherId,
                                   @Param("parentId") Integer parentId);
}