package com.example.travelappv2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AttractionDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    
    private String description;
    
    private String location;
    
    private Double price;
    
    private Integer rating;
    
    private String imageUrl;
    
    @NotNull(message = "La destination est obligatoire")
    private Long destinationId;
    
    private String destinationName;
    
    // Constructors
    public AttractionDTO() {
    }
    
    public AttractionDTO(Long id, String name, String description, String location, Double price, Integer rating, String imageUrl, Long destinationId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.destinationId = destinationId;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
