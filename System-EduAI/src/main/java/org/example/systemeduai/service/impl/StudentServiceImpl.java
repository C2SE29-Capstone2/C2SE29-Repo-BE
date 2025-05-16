package org.example.systemeduai.service.impl;

import org.example.systemeduai.dto.student.StudentMeasurementUpdateDto;
import org.example.systemeduai.dto.student.StudentUpdateDto;
import org.example.systemeduai.dto.student.StudentUserDetailDto;
import org.example.systemeduai.model.ContactBook;
import org.example.systemeduai.model.Student;
import org.example.systemeduai.repository.IContactBookRepository;
import org.example.systemeduai.repository.IStudentRepository;
import org.example.systemeduai.service.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class StudentServiceImpl implements IStudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IContactBookRepository contactBookRepository;

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public boolean existsByStudentPhone(String phone) {
        return studentRepository.existsByStudentPhone(phone);
    }

    @Override
    public Student findByAccountId(Integer accountId) {
        return studentRepository.findByAccountId(accountId);
    }

    @Override
    public StudentUserDetailDto findUserDetailByUsername(String username) {
        logger.info("Fetching details for username: {}", username);
        Tuple tuple = studentRepository.findUserDetailByUsername(username).orElse(null);
        if (tuple == null) {
            logger.warn("No student found for username: {}", username);
            return null;
        }
        logger.info("Successfully fetched details for username: {}", username);
        return StudentUserDetailDto.TupleToStudentDto(tuple);
    }

    @Override
    public StudentUserDetailDto updateStudentDetails(String username, StudentUpdateDto studentUpdateDto) {
        logger.info("Updating student details for username: {}", username);

        Tuple tuple = studentRepository.findUserDetailByUsername(username).orElse(null);
        if (tuple == null) {
            logger.warn("No student found for username: {}", username);
            return null;
        }

        Double currentHeight = tuple.get("height", Double.class);
        Double currentWeight = tuple.get("weight", Double.class);
        Integer studentId = tuple.get("student_id", Integer.class);
        String accountEmail = tuple.get("email", String.class);

        if (studentId == null) {
            logger.warn("Student ID is null for username: {}", username);
            return null;
        }

        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            logger.warn("Student with ID {} not found in the database", studentId);
            return null;
        }

        Student student = studentOptional.get();
        student.setStudentName(studentUpdateDto.getStudentName());
        student.setStudentGender(studentUpdateDto.getStudentGender());
        student.setDateOfBirth(studentUpdateDto.getDateOfBirth());

        LocalDate birthDate = studentUpdateDto.getDateOfBirth().toLocalDate();
        LocalDate currentDate = LocalDate.now();
        int calculatedAge = Period.between(birthDate, currentDate).getYears();
        student.setAge(calculatedAge);

        student.setStudentAddress(studentUpdateDto.getStudentAddress());
        student.setProfileImage(studentUpdateDto.getProfileImage());
        student.setHealthStatus(studentUpdateDto.getHealthStatus());
        student.setHobby(studentUpdateDto.getHobby());

        studentRepository.save(student);
        logger.info("Successfully updated student details for username: {}", username);

        return new StudentUserDetailDto(
                student.getStudentId(),
                student.getStudentName(),
                student.getStudentGender(),
                student.getDateOfBirth(),
                student.getAge(),
                student.getStudentAddress(),
                student.getProfileImage(),
                student.getHealthStatus(),
                student.getHobby(),
                currentHeight,
                currentWeight,
                username,
                accountEmail
        );
    }

    @Override
    public StudentUserDetailDto updateStudentMeasurements(String username, StudentMeasurementUpdateDto measurementUpdateDto) {
        logger.info("Updating student measurements for username: {}", username);

        Tuple tuple = studentRepository.findUserDetailByUsername(username).orElse(null);
        if (tuple == null) {
            logger.warn("No student found for username: {}", username);
            return null;
        }

        Integer studentId = tuple.get("student_id", Integer.class);
        String accountEmail = tuple.get("email", String.class);

        if (studentId == null) {
            logger.warn("Student ID is null for username: {}", username);
            return null;
        }

        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            logger.warn("Student with ID {} not found in the database", studentId);
            return null;
        }

        Student student = studentOptional.get();
        Optional<ContactBook> contactBookOptional = contactBookRepository.findByStudentStudentId(studentId);
        ContactBook contactBook = getContactBook(measurementUpdateDto, contactBookOptional, student);

        contactBookRepository.save(contactBook);
        logger.info("Successfully updated student measurements for username: {}", username);

        return new StudentUserDetailDto(
                student.getStudentId(),
                student.getStudentName(),
                student.getStudentGender(),
                student.getDateOfBirth(),
                student.getAge(),
                student.getStudentAddress(),
                student.getProfileImage(),
                student.getHealthStatus(),
                student.getHobby(),
                contactBook.getHeight(),
                contactBook.getWeight(),
                username,
                accountEmail
        );
    }

    private static ContactBook getContactBook(StudentMeasurementUpdateDto measurementUpdateDto,
                                              Optional<ContactBook> contactBookOptional, Student student) {
        ContactBook contactBook;
        if (contactBookOptional.isPresent()) {
            contactBook = contactBookOptional.get();
            if (measurementUpdateDto.getHeight() != null) {
                contactBook.setHeight(measurementUpdateDto.getHeight());
            }
            if (measurementUpdateDto.getWeight() != null) {
                contactBook.setWeight(measurementUpdateDto.getWeight());
            }
        } else {
            contactBook = new ContactBook();
            contactBook.setHeight(measurementUpdateDto.getHeight());
            contactBook.setWeight(measurementUpdateDto.getWeight());
            contactBook.setStudent(student);
        }
        return contactBook;
    }
}
