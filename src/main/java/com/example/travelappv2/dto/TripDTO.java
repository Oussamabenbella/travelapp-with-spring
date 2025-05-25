package com.example.travelappv2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class TripDTO {
    
    private Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String title;
    
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate startDate;
    
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate endDate;
    
    private Double budget;
    
    private String status;
    
    private String notes;
    
    @NotNull(message = "L'utilisateur est obligatoire")
    private Long userId;
    
    private String username;
    
    @NotNull(message = "La destination est obligatoire")
    private Long destinationId;
    
    private String destinationName;
    
    private Set<Long> attractionIds = new HashSet<>();
    
    // Constructors
    public TripDTO() {
    }
    
    public TripDTO(Long id, String title, LocalDate startDate, LocalDate endDate, Double budget, String status, String notes, Long userId, Long destinationId) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.status = status;
        this.notes = notes;
        this.userId = userId;
        this.destinationId = destinationId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public Double getBudget() {
        return budget;
    }
    
    public void setBudget(Double budget) {
        this.budget = budget;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getDestinationId() {
        return destinationId;
    }
    
    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
    
    public String getDestinationName() {
        return destinationName;
    }
    
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
    
    public Set<Long> getAttractionIds() {
        return attractionIds;
    }
    
    public void setAttractionIds(Set<Long> attractionIds) {
        this.attractionIds = attractionIds;
    }
}
