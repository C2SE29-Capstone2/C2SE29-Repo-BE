package org.example.systemeduai.repository;

import org.example.systemeduai.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface IParentRepository extends JpaRepository<Parent, Integer> {

    @Query(value = "SELECT p.* FROM parent p WHERE p.student_id = :studentId", nativeQuery = true)
    Optional<Parent> findByStudentId(@Param("studentId") Integer studentId);
}
