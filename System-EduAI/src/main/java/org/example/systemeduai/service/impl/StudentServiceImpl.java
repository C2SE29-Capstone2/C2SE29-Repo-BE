package org.example.systemeduai.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.systemeduai.dto.request.CreateStudentRequest;
import org.example.systemeduai.dto.request.UpdateStudentRequest;
import org.example.systemeduai.dto.response.PageResponse;
import org.example.systemeduai.dto.response.StudentResponse;
import org.example.systemeduai.dto.student.StudentMeasurementUpdateDto;
import org.example.systemeduai.dto.student.StudentUpdateDto;
import org.example.systemeduai.dto.student.StudentUserDetailDto;
import org.example.systemeduai.exception.ResourceNotFoundException;
import org.example.systemeduai.model.*;
import org.example.systemeduai.repository.*;
import org.example.systemeduai.service.IStudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements IStudentService {
    private final IStudentRepository studentRepository;
    private final IClassroomRepository classroomRepository;
    private final IContactBookRepository contactBookRepository;
    private final IHealthRecordRepository healthRecordRepository;
    private final IAccountRepository accountRepository;
    private final IParentRepository parentRepository;

    @Override
    public StudentResponse getStudentById(Integer studentId) {
        return studentRepository.findById(studentId)
                .map(this::mapStudentToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
    }

    @Override
    public PageResponse<StudentResponse> getStudents(int page, int size, String sortBy, String sortDir) {
        Sort.Direction dir = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, dir, sortBy);
        Page<Student> students = studentRepository.findAllStudents(pageable);
        return mapStudentPageToResponse(students);
    }

    @Override
    public PageResponse<StudentResponse> getStudentsByClassroomId(Integer classroomId, int page, int size, String sortBy, String sortDir) {
        Sort.Direction dir = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, dir, sortBy);
        Page<Student> students = studentRepository.findByClassroomClassroomId(classroomId, pageable);
        return mapStudentPageToResponse(students);
    }

    @Override
    public void updateStudent(UpdateStudentRequest request, Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        log.info("Updating student with ID: {}", studentId);
        student.setStudentName(request.getStudentName());
        student.setStudentEmail(request.getStudentEmail());
        student.setStudentPhone(request.getStudentPhone());
        student.setStudentGender(request.getStudentGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAge(request.getAge());
        student.setStudentAddress(request.getStudentAddress());
        student.setProfileImage(request.getProfileImage());
        student.setHealthStatus(request.getHealthStatus());
        student.setHobby(request.getHobby());
        Classroom classroom = request.getClassroomId() == null ? null :
                classroomRepository.findById(request.getClassroomId())
                        .orElseThrow(() -> new ResourceNotFoundException("Classroom", "id", request.getClassroomId()));
        student.setClassroom(classroom);
        studentRepository.save(student);
        log.info("Successfully updated student with ID: {}", studentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudent(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        Parent parent = parentRepository.findByStudentStudentId(studentId).orElse(null);
        if (parent != null) {
            parent.setStudent(null);
            parentRepository.save(parent);
        }
        Account account = student.getAccount();
        accountRepository.deleteById(account.getAccountId());
        healthRecordRepository.deleteByStudentStudentId(studentId);
        contactBookRepository.deleteByStudentStudentId(studentId);
        log.info("Successfully deleted student with ID: {}", studentId);
    }

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
        log.info("Fetching details for username: {}", username);
        Tuple tuple = studentRepository.findUserDetailByUsername(username).orElse(null);
        if (tuple == null) {
            log.warn("No student found for username: {}", username);
            return null;
        }
        log.info("Successfully fetched details for username: {}", username);
        return StudentUserDetailDto.TupleToStudentDto(tuple);
    }

    @Override
    public StudentUserDetailDto updateStudentDetails(String username, StudentUpdateDto studentUpdateDto) {
        log.info("Updating student details for username: {}", username);

        Tuple tuple = studentRepository.findUserDetailByUsername(username).orElse(null);
        if (tuple == null) {
            log.warn("No student found for username: {}", username);
            return null;
        }

        Double currentHeight = tuple.get("height", Double.class);
        Double currentWeight = tuple.get("weight", Double.class);
        Integer studentId = tuple.get("student_id", Integer.class);
        String accountEmail = tuple.get("email", String.class);

        if (studentId == null) {
            log.warn("Student ID is null for username: {}", username);
            return null;
        }

        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            log.warn("Student with ID {} not found in the database", studentId);
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
        log.info("Successfully updated student details for username: {}", username);

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
        log.info("Updating student measurements for username: {}", username);

        Tuple tuple = studentRepository.findUserDetailByUsername(username).orElse(null);
        if (tuple == null) {
            log.warn("No student found for username: {}", username);
            return null;
        }

        Integer studentId = tuple.get("student_id", Integer.class);
        String accountEmail = tuple.get("email", String.class);

        if (studentId == null) {
            log.warn("Student ID is null for username: {}", username);
            return null;
        }

        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            log.warn("Student with ID {} not found in the database", studentId);
            return null;
        }

        Student student = studentOptional.get();
        Optional<ContactBook> contactBookOptional = contactBookRepository.findByStudentStudentId(studentId);
        ContactBook contactBook = getContactBook(measurementUpdateDto, contactBookOptional, student);

        contactBookRepository.save(contactBook);
        log.info("Successfully updated student measurements for username: {}", username);

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

    private PageResponse<StudentResponse> mapStudentPageToResponse(Page<Student> studentPage) {
        PageResponse<StudentResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(studentPage.getContent().stream()
                .map(this::mapStudentToResponse)
                .toList());
        pageResponse.setPageNumber(studentPage.getNumber());
        pageResponse.setPageSize(studentPage.getSize());
        pageResponse.setTotalElements(studentPage.getTotalElements());
        pageResponse.setTotalPages(studentPage.getTotalPages());
        pageResponse.setFirstPage(studentPage.isFirst());
        pageResponse.setLastPage(studentPage.isLast());
        return pageResponse;
    }

    private StudentResponse mapStudentToResponse(Student student) {
        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .studentName(student.getStudentName())
                .studentGender(student.getStudentGender())
                .studentEmail(student.getStudentEmail())
                .studentPhone(student.getStudentPhone())
                .dateOfBirth(student.getDateOfBirth())
                .age(student.getAge())
                .studentAddress(student.getStudentAddress())
                .profileImage(student.getProfileImage())
                .healthStatus(student.getHealthStatus())
                .hobby(student.getHobby())
                .accountId(student.getAccount().getAccountId())
                .classroomId(student.getClassroom() != null ? student.getClassroom().getClassroomId() : null)
                .build();
    }
}
