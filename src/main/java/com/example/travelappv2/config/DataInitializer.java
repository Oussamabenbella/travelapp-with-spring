package com.example.travelappv2.config;

import com.example.travelappv2.entity.Role;
import com.example.travelappv2.entity.User;
import com.example.travelappv2.repository.RoleRepository;
import com.example.travelappv2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialiser les rôles s'ils n'existent pas déjà
        createRoleIfNotFound("USER");
        createRoleIfNotFound("ADMIN");

        // Créer un compte administrateur par défaut
        if (userRepository.findByUsername("oussama").isEmpty()) {
            User admin = new User();
            admin.setUsername("oussama");
            admin.setEmail("oussama@travelapp.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ADMIN").orElseThrow());
            roles.add(roleRepository.findByName("USER").orElseThrow());
            admin.setRoles(roles);
            
            userRepository.save(admin);
            System.out.println("Utilisateur Oussama créé");
        }
        
        // Créer un utilisateur standard par défaut
        if (userRepository.findByUsername("haitam").isEmpty()) {
            User user = new User();
            user.setUsername("haitam");
            user.setEmail("haitam@travelapp.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEnabled(true);
            
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("USER").orElseThrow());
            user.setRoles(roles);
            
            userRepository.save(user);
            System.out.println("Utilisateur Haitam créé");
        }
    }

    private void createRoleIfNotFound(String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role(name);
            roleRepository.save(role);
            System.out.println("Rôle " + name + " créé");
        }
    }
}
