package org.example.systemeduai.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.example.systemeduai.dto.request.LoginRequest;
import org.example.systemeduai.dto.request.SignupRequest;
import org.example.systemeduai.dto.response.JwtResponse;
import org.example.systemeduai.dto.response.MessageResponse;
import org.example.systemeduai.dto.response.SocialResponse;
import org.example.systemeduai.model.Account;
import org.example.systemeduai.model.Role;
import org.example.systemeduai.model.Student;
import org.example.systemeduai.security.jwt.JwtTokenProvider;
import org.example.systemeduai.security.jwt.JwtUtility;
import org.example.systemeduai.security.userprinciple.UserPrinciple;
import org.example.systemeduai.service.IAccountService;
import org.example.systemeduai.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/public")
public class SecurityController {

    @Value("${google.clientId}")
    String googleClientId;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IStudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtility.generateJwtToken(authentication);
        UserPrinciple userDetails = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new JwtResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        roles
                )
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (accountService.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("The username existed! Please try again!"), HttpStatus.OK);
        }

        if (accountService.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>(new MessageResponse("The email existed! Please try again!"), HttpStatus.OK);
        }

        if (studentService.existsByStudentPhone(signupRequest.getPhone())) {
            return new ResponseEntity<>(new MessageResponse("The phone number existed! Please try again!"), HttpStatus.OK);
        }

        LocalDate birthDate = signupRequest.getDateOfBirth().toLocalDate();
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthDate, currentDate).getYears();

        if (age < 3 || age > 6) {
            return new ResponseEntity<>(new MessageResponse("The student must be between 3 and 6 years old!"), HttpStatus.OK);
        }

        Account account = new Account();
        account.setUsername(signupRequest.getUsername());
        account.setEncryptPassword(passwordEncoder.encode(signupRequest.getPassword()));
        account.setEmail(signupRequest.getEmail());
        account.setIsEnable(true);
        accountService.save(account);

        Role role = new Role(3, "ROLE_STUDENT");
        Set<Role> tempRoles = account.getRoles();
        tempRoles.add(role);
        account.setRoles(tempRoles);

        Student student = new Student(
                signupRequest.getName(),
                signupRequest.getEmail(),
                signupRequest.getPhone(),
                signupRequest.getGender(),
                signupRequest.getDateOfBirth(),
                signupRequest.getAddress(),
                account
        );

        student.setAge(age);
        studentService.save(student);

        return new ResponseEntity<>(new MessageResponse("Account registration successful!"), HttpStatus.OK);
    }

    @PostMapping("/oauth/google")
    public ResponseEntity<?> loginGoogle(@RequestBody SocialResponse jwtResponseSocial) {
        final NetHttpTransport netHttpTransport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder builder =
                new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory)
                        .setAudience(Collections.singletonList(googleClientId));

        try {
            final GoogleIdToken googleIdToken = GoogleIdToken.parse(builder.getJsonFactory(), jwtResponseSocial.getToken());
            final GoogleIdToken.Payload payload = googleIdToken.getPayload();

            Account existingAccount = accountService.findByEmail(payload.getEmail());

            if (existingAccount == null) {
                Account newAccount = new Account();
                newAccount.setEmail(payload.getEmail());
                newAccount.setUsername(payload.getEmail());
                newAccount.setIsEnable(true);
                newAccount.setRoles(Collections.singleton(new Role(3, "ROLE_STUDENT")));

                newAccount = accountService.save(newAccount);

                Student newStudent = new Student();
                newStudent.setStudentName(payload.get("name").toString());
                newStudent.setStudentEmail(payload.getEmail());

                String pictureUrl = payload.get("picture") != null ? payload.get("picture").toString() : null;
                newStudent.setProfileImage(pictureUrl);

                newStudent .setAccount(newAccount);
                studentService.save(newStudent);
            }
            String jwt = jwtTokenProvider.generateToken(payload.getEmail());
            return ResponseEntity.ok(new JwtResponse(jwt, payload.getEmail()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/forgot-password/{username}")
    public ResponseEntity<?> forgotPassword(@PathVariable String username) {
        Account account = accountService.findByUsername(username);
        Map<String, String> listError = new HashMap<>();
        if (account == null) {
            listError.put("nonExistUsername", "Tên đăng nhập không tồn tại, vui lòng chọn tên đăng nhập khác!");
            return ResponseEntity.badRequest().body(listError);
        }

        Student student = studentService.findByAccountId(account.getAccountId());
        if (student == null || student.getStudentEmail() == null) {
            listError.put("noEmail", "Không tìm thấy email liên kết với tài khoản này!");
            return ResponseEntity.badRequest().body(listError);
        }

        String email = student.getStudentEmail();
        String code = accountService.generateCode();
        accountService.storeOtp(username, code);
        accountService.sendEmailOTP(email, code);
        return ResponseEntity.ok(Map.of("message", "Mã OTP đã được gửi đến email của bạn."));
    }
    @PostMapping("/reset-password/{username}")
    public ResponseEntity<?> resetPassword(@PathVariable String username,
                                           @RequestBody Map<String, String> request) {
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");
        Map<String, String> listError = new HashMap<>();

        if (otp == null || otp.isEmpty()) {
            listError.put("invalidOtp", "Vui lòng cung cấp mã OTP!");
        }
        if (newPassword == null || newPassword.length() < 8) {
            listError.put("invalidPassword", "Mật khẩu mới phải có ít nhất 8 ký tự!");
        }
        if (!listError.isEmpty()) {
            return ResponseEntity.badRequest().body(listError);
        }

        if (!accountService.verifyOtp(username, otp)) {
            listError.put("invalidOtp", "Mã OTP không hợp lệ hoặc đã hết hạn!");
            return ResponseEntity.badRequest().body(listError);
        }

        Account account = accountService.findByUsername(username);
        if (account == null) {
            listError.put("nonExistUsername", "Tên đăng nhập không tồn tại!");
            return ResponseEntity.badRequest().body(listError);
        }

        accountService.changePasswordByForgot(newPassword, account);
        accountService.clearOtp(username);
        return ResponseEntity.ok(Map.of("message", "Đặt lại mật khẩu thành công!"));
    }
}
