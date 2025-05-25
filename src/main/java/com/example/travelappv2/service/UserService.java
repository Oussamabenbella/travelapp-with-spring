package com.example.travelappv2.service;

import com.example.travelappv2.entity.Role;
import com.example.travelappv2.entity.User;
import com.example.travelappv2.repository.RoleRepository;
import com.example.travelappv2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur: " + username));
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Transactional
    public User registerUser(User user, String roleName) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Nom d'utilisateur déjà utilisé");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }
        
        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Attribuer le rôle
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé: " + roleName));
        user.addRole(role);
        
        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
