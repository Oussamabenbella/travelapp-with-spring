package com.example.travelappv2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DestinationDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    
    @NotBlank(message = "Le pays est obligatoire")
    private String country;
    
    private String description;
    
    @NotNull(message = "La latitude est obligatoire")
    private Double latitude;
    
    @NotNull(message = "La longitude est obligatoire")
    private Double longitude;
    
    // Constructors
    public DestinationDTO() {
    }
    
    public DestinationDTO(Long id, String name, String country, String description, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
