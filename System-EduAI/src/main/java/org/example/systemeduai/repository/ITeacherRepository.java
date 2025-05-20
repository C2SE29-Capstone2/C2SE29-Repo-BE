package org.example.systemeduai.repository;

import org.example.systemeduai.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ITeacherRepository extends JpaRepository<Teacher, Integer> {
    @Query("SELECT t FROM Teacher t")
    Page<Teacher> findAllTeachers(Pageable pageable);
    List<Teacher> findByTeacherIdIn(List<Integer> teacherIds);
    @Query(value = "SELECT t.* FROM teacher t " +
            "JOIN account a ON t.account_id = a.account_id " +
            "WHERE a.username = :username", nativeQuery = true)
    Optional<Teacher> findByUsername(@Param("username") String username);

}
