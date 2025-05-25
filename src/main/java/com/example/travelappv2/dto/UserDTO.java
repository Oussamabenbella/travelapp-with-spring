package com.example.travelappv2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
    
    private Set<String> roles = new HashSet<>();
    
    // Constructors
    public UserDTO() {
    }
    
    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Set<String> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
