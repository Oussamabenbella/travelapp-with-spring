package com.example.travelappv2.controller.api;

import com.example.travelappv2.dto.UserDTO;
import com.example.travelappv2.entity.User;
import com.example.travelappv2.exception.ResourceNotFoundException;
import com.example.travelappv2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        List<UserDTO> userDTOs = userService.getAllUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return new ResponseEntity<>(convertToDTO(user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userService.saveUser(user);
        
        return new ResponseEntity<>(convertToDTO(savedUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        
        User updatedUser = userService.saveUser(user);
        
        return new ResponseEntity<>(convertToDTO(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        userService.deleteUser(id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        
        // Ne pas inclure le mot de passe dans la réponse
        user.getRoles().forEach(role -> dto.getRoles().add(role.getName()));
        
        return dto;
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        
        // Gestion des rôles à implémenter dans le service
        
        return user;
    }
}
