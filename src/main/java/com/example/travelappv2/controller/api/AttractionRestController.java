package com.example.travelappv2.controller.api;

import com.example.travelappv2.dto.AttractionDTO;
import com.example.travelappv2.entity.Attraction;
import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.exception.ResourceNotFoundException;
import com.example.travelappv2.service.AttractionService;
import com.example.travelappv2.service.DestinationService;
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
@RequestMapping("/api/attractions")
public class AttractionRestController {

    @Autowired
    private AttractionService attractionService;
    
    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public ResponseEntity<List<AttractionDTO>> getAllAttractions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        List<AttractionDTO> attractionDTOs = attractionService.getAllAttractions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(attractionDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttractionDTO> getAttractionById(@PathVariable Long id) {
        Attraction attraction = attractionService.getAttractionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction", "id", id));
        
        return new ResponseEntity<>(convertToDTO(attraction), HttpStatus.OK);
    }
    
    @GetMapping("/destination/{destinationId}")
    public ResponseEntity<List<AttractionDTO>> getAttractionsByDestination(@PathVariable Long destinationId) {
        Destination destination = destinationService.getDestinationById(destinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", destinationId));
                
        List<AttractionDTO> attractionDTOs = attractionService.getAllAttractions().stream()
                .filter(attraction -> attraction.getDestination() != null && attraction.getDestination().getId().equals(destinationId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
        return new ResponseEntity<>(attractionDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AttractionDTO> createAttraction(@Valid @RequestBody AttractionDTO attractionDTO) {
        Destination destination = destinationService.getDestinationById(attractionDTO.getDestinationId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", attractionDTO.getDestinationId()));
        
        Attraction attraction = convertToEntity(attractionDTO);
        attraction.setDestination(destination);
        
        Attraction savedAttraction = attractionService.saveAttraction(attraction);
        
        return new ResponseEntity<>(convertToDTO(savedAttraction), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttractionDTO> updateAttraction(@PathVariable Long id, @Valid @RequestBody AttractionDTO attractionDTO) {
        Attraction attraction = attractionService.getAttractionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction", "id", id));
        
        Destination destination = destinationService.getDestinationById(attractionDTO.getDestinationId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", attractionDTO.getDestinationId()));
        
        attraction.setName(attractionDTO.getName());
        attraction.setDescription(attractionDTO.getDescription());
        attraction.setLocation(attractionDTO.getLocation());
        attraction.setPrice(attractionDTO.getPrice());
        attraction.setRating(attractionDTO.getRating());
        attraction.setImageUrl(attractionDTO.getImageUrl());
        attraction.setDestination(destination);
        
        Attraction updatedAttraction = attractionService.saveAttraction(attraction);
        
        return new ResponseEntity<>(convertToDTO(updatedAttraction), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttraction(@PathVariable Long id) {
        Attraction attraction = attractionService.getAttractionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction", "id", id));
        
        attractionService.deleteAttraction(id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private AttractionDTO convertToDTO(Attraction attraction) {
        AttractionDTO dto = new AttractionDTO();
        dto.setId(attraction.getId());
        dto.setName(attraction.getName());
        dto.setDescription(attraction.getDescription());
        dto.setLocation(attraction.getLocation());
        dto.setPrice(attraction.getPrice());
        dto.setRating(attraction.getRating());
        dto.setImageUrl(attraction.getImageUrl());
        
        if (attraction.getDestination() != null) {
            dto.setDestinationId(attraction.getDestination().getId());
            dto.setDestinationName(attraction.getDestination().getName());
        }
        
        return dto;
    }

    private Attraction convertToEntity(AttractionDTO dto) {
        Attraction attraction = new Attraction();
        attraction.setId(dto.getId());
        attraction.setName(dto.getName());
        attraction.setDescription(dto.getDescription());
        attraction.setLocation(dto.getLocation());
        attraction.setPrice(dto.getPrice());
        attraction.setRating(dto.getRating());
        attraction.setImageUrl(dto.getImageUrl());
        
        return attraction;
    }
}
