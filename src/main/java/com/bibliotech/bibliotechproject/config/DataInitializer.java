package com.bibliotech.bibliotechproject.config;

import com.bibliotech.bibliotechproject.models.User;
import com.bibliotech.bibliotechproject.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            // We delete everything first to ensure no duplicates
            userRepository.deleteAll()
                    .then(userRepository.save(new User(null, "admin", encoder.encode("admin123"), Set.of("ROLE_ADMIN"))))
                    .then(userRepository.save(new User(null, "member1", encoder.encode("pass123"), Set.of("ROLE_USER"))))
                    .subscribe(user -> System.out.println("DEBUG: Created user: " + user.getUsername()));
        };
    }
}