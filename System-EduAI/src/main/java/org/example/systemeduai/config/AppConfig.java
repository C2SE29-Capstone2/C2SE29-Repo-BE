package org.example.systemeduai.config;

import lombok.RequiredArgsConstructor;
import org.example.systemeduai.model.Role;
import org.example.systemeduai.repository.IRoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements ApplicationRunner {
    private final IRoleRepository roleRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.saveAllAndFlush(List.of(
                    new Role(1, "ROLE_ADMIN"),
                    new Role(2, "ROLE_TEACHER"),
                    new Role(3, "ROLE_STUDENT")
            ));
        }
    }
}
