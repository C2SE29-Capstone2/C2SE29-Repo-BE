package org.example.systemeduai.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.systemeduai.dto.request.CreateTeacherRequest;
import org.example.systemeduai.dto.request.UpdateTeacherRequest;
import org.example.systemeduai.dto.response.PageResponse;
import org.example.systemeduai.dto.response.TeacherResponse;
import org.example.systemeduai.exception.ResourceNotFoundException;
import org.example.systemeduai.model.Account;
import org.example.systemeduai.model.Role;
import org.example.systemeduai.model.Teacher;
import org.example.systemeduai.repository.*;
import org.example.systemeduai.service.ITeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements ITeacherService {
    private final ITeacherRepository teacherRepository;
    private final IClassroomTeacherRepository classroomTeacherRepository;
    private final IContactBookRepository contactBookRepository;
    private final IAccountRepository accountRepository;
    private final BCryptPasswordEncoder encoder;
    private final IRoleRepository roleRepository;
    @Override
    public TeacherResponse getTeacherById(Integer teacherId) {
        return mapToResponse(teacherRepository.findById(teacherId).orElseThrow(
                () -> new ResourceNotFoundException("Teacher", "teacherId", teacherId)
        ));
    }

    @Override
    public PageResponse<TeacherResponse> getTeachers(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);
        return mapToPageResponse(teacherPage);
    }

    @Override
    public void createTeacher(CreateTeacherRequest request) {
        log.info("Creating new teacher with name: {}", request.getTeacherName());
        Teacher teacher = Teacher.builder()
                .teacherName(request.getTeacherName())
                .teacherEmail(request.getTeacherEmail())
                .teacherPhone(request.getTeacherPhone())
                .teacherGender(request.getTeacherGender())
                .dateOfBirth(request.getDateOfBirth())
                .teacherAddress(request.getTeacherAddress())
                .profileImage(request.getProfileImage())
                .qualifications(request.getQualifications())
                .build();
        String normalizedName = Normalizer.normalize(request.getTeacherName(), Normalizer.Form.NFD)
                .replace(" ","").toLowerCase();
        List<Account> existingAccounts = accountRepository.findByUsernameLike(normalizedName);
        if (existingAccounts.size() > 0) {
            normalizedName += existingAccounts.size();
        }
        String uuid = UUID.randomUUID().toString();
        // TODO: Implement strategy to generate password and notice to teacher
        Account account = Account.builder()
                .username(normalizedName)
                .encryptPassword(encoder.encode(uuid))
                .email(request.getTeacherEmail())
                .isEnable(true)
                .build();
        Role roleTeacher = roleRepository.findByRoleName("ROLE_TEACHER").orElse(
                Role.builder()
                        .roleName("ROLE_TEACHER")
                        .build());
        account.getRoles().add(roleTeacher);
        accountRepository.save(account);
        teacher.setAccount(account);
        teacherRepository.save(teacher);
        log.info("Teacher with name: {} created successfully", request.getTeacherName());
    }

    @Override
    public void updateTeacher(Integer teacherId, UpdateTeacherRequest request) {
        log.info("Updating teacher with ID: {}", teacherId);
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new ResourceNotFoundException("Teacher", "teacherId", teacherId)
        );
        teacher.setTeacherName(request.getTeacherName());
        teacher.setTeacherEmail(request.getTeacherEmail());
        teacher.setTeacherPhone(request.getTeacherPhone());
        teacher.setTeacherGender(request.getTeacherGender());
        teacher.setDateOfBirth(request.getDateOfBirth());
        teacher.setTeacherAddress(request.getTeacherAddress());
        teacher.setProfileImage(request.getProfileImage());
        teacher.setQualifications(request.getQualifications());
        teacherRepository.save(teacher);
        log.info("Teacher with ID: {} updated successfully", teacherId);
    }

    @Override
    public void deleteTeacher(Integer teacherId) {
        log.info("Deleting teacher with ID: {}", teacherId);
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new ResourceNotFoundException("Teacher", "teacherId", teacherId)
        );
        Account account = teacher.getAccount();
        if (account != null)
            accountRepository.deleteById(account.getAccountId());
        classroomTeacherRepository.deleteByTeacherTeacherId(teacherId);
        contactBookRepository.deleteByTeacherId(teacherId);
        teacherRepository.deleteById(teacherId);
        log.info("Teacher with ID: {} deleted successfully", teacherId);
    }

    private TeacherResponse mapToResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .teacherId(teacher.getTeacherId())
                .teacherName(teacher.getTeacherName())
                .teacherEmail(teacher.getTeacherEmail())
                .teacherPhone(teacher.getTeacherPhone())
                .teacherGender(teacher.getTeacherGender())
                .dateOfBirth(teacher.getDateOfBirth())
                .teacherAddress(teacher.getTeacherAddress())
                .profileImage(teacher.getProfileImage())
                .qualifications(teacher.getQualifications())
                .accountId(teacher.getAccount().getAccountId())
                .build();
    }

    private PageResponse<TeacherResponse> mapToPageResponse(Page<Teacher> pageResponse) {
        return PageResponse.<TeacherResponse>builder()
                .content(pageResponse.getContent().stream().map(this::mapToResponse).toList())
                .pageNumber(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .isFirstPage(pageResponse.isFirst())
                .isLastPage(pageResponse.isLast())
                .build();
    }
}
