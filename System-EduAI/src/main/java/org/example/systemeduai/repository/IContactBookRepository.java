package org.example.systemeduai.repository;

import org.example.systemeduai.model.ContactBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface IContactBookRepository extends JpaRepository<ContactBook, Integer> {
    Optional<ContactBook> findByStudentStudentId(Integer studentId);
    @Modifying
    @Transactional
    void deleteByStudentStudentId(Integer studentId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM contact_book_teachers WHERE teacher_id = :teacherId", nativeQuery = true)
    void deleteByTeacherId(@Param("teacherId") Integer teacherId);
}
