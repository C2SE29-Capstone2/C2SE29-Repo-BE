package org.example.systemeduai.repository;

import org.example.systemeduai.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.Optional;

@Repository
@Transactional
public interface ITeacherRepository extends JpaRepository<Teacher, Integer> {
    @Query(value = "SELECT t.* FROM teacher t " +
            "JOIN account a ON t.account_id = a.account_id " +
            "WHERE a.username = :username", nativeQuery = true)
    Optional<Teacher> findByUsername(@Param("username") String username);

    @Query(value = "SELECT t.teacher_id, t.teacher_name, t.teacher_phone, t.teacher_gender, t.date_of_birth, " +
            "t.teacher_address, t.profile_image, t.qualifications, a.username, a.email " +
            "FROM teacher t " +
            "INNER JOIN account a ON t.account_id = a.account_id " +
            "WHERE a.is_enable = true AND a.username = :username", nativeQuery = true)
    Optional<Tuple> findUserDetailByUsername(@Param("username") String username);

    @Query("SELECT t FROM Teacher t WHERE t.account.accountId = :accountId")
    Optional<Teacher> findByAccountId(@Param("accountId") Integer accountId);
}
