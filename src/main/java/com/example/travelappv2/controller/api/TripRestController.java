package com.example.travelappv2.controller.api;

import com.example.travelappv2.dto.TripDTO;
import com.example.travelappv2.entity.Attraction;
import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.entity.Trip;
import com.example.travelappv2.entity.User;
import com.example.travelappv2.exception.ResourceNotFoundException;
import com.example.travelappv2.service.AttractionService;
import com.example.travelappv2.service.DestinationService;
import com.example.travelappv2.service.TripService;
import com.example.travelappv2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trips")
public class TripRestController {

    @Autowired
    private TripService tripService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DestinationService destinationService;
    
    @Autowired
    private AttractionService attractionService;

    @GetMapping
    public ResponseEntity<List<TripDTO>> getAllTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        List<TripDTO> tripDTOs = tripService.getAllTrips().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(tripDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getTripById(@PathVariable Long id) {
        Trip trip = tripService.getTripById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
        
        return new ResponseEntity<>(convertToDTO(trip), HttpStatus.OK);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TripDTO>> getTripsByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                
        List<TripDTO> tripDTOs = tripService.getAllTrips().stream()
                .filter(trip -> trip.getUser() != null && trip.getUser().getId().equals(userId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
        return new ResponseEntity<>(tripDTOs, HttpStatus.OK);
    }
    
    @GetMapping("/destination/{destinationId}")
    public ResponseEntity<List<TripDTO>> getTripsByDestination(@PathVariable Long destinationId) {
        Destination destination = destinationService.getDestinationById(destinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", destinationId));
                
        List<TripDTO> tripDTOs = tripService.getAllTrips().stream()
                .filter(trip -> trip.getDestination() != null && trip.getDestination().getId().equals(destinationId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
        return new ResponseEntity<>(tripDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TripDTO> createTrip(@Valid @RequestBody TripDTO tripDTO) {
        User user = userService.getUserById(tripDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", tripDTO.getUserId()));
        
        Destination destination = destinationService.getDestinationById(tripDTO.getDestinationId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", tripDTO.getDestinationId()));
        
        Trip trip = convertToEntity(tripDTO);
        trip.setUser(user);
        trip.setDestination(destination);
        
        // Ajouter des attractions au voyage
        if (tripDTO.getAttractionIds() != null && !tripDTO.getAttractionIds().isEmpty()) {
            Set<Attraction> attractions = new HashSet<>();
            for (Long attractionId : tripDTO.getAttractionIds()) {
                Attraction attraction = attractionService.getAttractionById(attractionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Attraction", "id", attractionId));
                attractions.add(attraction);
            }
            trip.setAttractions(attractions);
        }
        
        Trip savedTrip = tripService.saveTrip(trip);
        
        return new ResponseEntity<>(convertToDTO(savedTrip), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripDTO> updateTrip(@PathVariable Long id, @Valid @RequestBody TripDTO tripDTO) {
        Trip trip = tripService.getTripById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
        
        User user = userService.getUserById(tripDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", tripDTO.getUserId()));
        
        Destination destination = destinationService.getDestinationById(tripDTO.getDestinationId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", tripDTO.getDestinationId()));
        
        trip.setTitle(tripDTO.getTitle());
        trip.setStartDate(tripDTO.getStartDate());
        trip.setEndDate(tripDTO.getEndDate());
        trip.setBudget(tripDTO.getBudget());
        trip.setStatus(tripDTO.getStatus());
        trip.setNotes(tripDTO.getNotes());
        trip.setUser(user);
        trip.setDestination(destination);
        
        // Mettre à jour les attractions du voyage
        if (tripDTO.getAttractionIds() != null) {
            Set<Attraction> attractions = new HashSet<>();
            for (Long attractionId : tripDTO.getAttractionIds()) {
                Attraction attraction = attractionService.getAttractionById(attractionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Attraction", "id", attractionId));
                attractions.add(attraction);
            }
            trip.setAttractions(attractions);
        }
        
        Trip updatedTrip = tripService.saveTrip(trip);
        
        return new ResponseEntity<>(convertToDTO(updatedTrip), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        Trip trip = tripService.getTripById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
        
        tripService.deleteTrip(id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private TripDTO convertToDTO(Trip trip) {
        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        dto.setTitle(trip.getTitle());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setBudget(trip.getBudget());
        dto.setStatus(trip.getStatus());
        dto.setNotes(trip.getNotes());
        
        if (trip.getUser() != null) {
            dto.setUserId(trip.getUser().getId());
            dto.setUsername(trip.getUser().getUsername());
        }
        
        if (trip.getDestination() != null) {
            dto.setDestinationId(trip.getDestination().getId());
            dto.setDestinationName(trip.getDestination().getName());
        }
        
        if (trip.getAttractions() != null) {
            trip.getAttractions().forEach(attraction -> dto.getAttractionIds().add(attraction.getId()));
        }
        
        return dto;
    }

    private Trip convertToEntity(TripDTO dto) {
        Trip trip = new Trip();
        trip.setId(dto.getId());
        trip.setTitle(dto.getTitle());
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());
        trip.setBudget(dto.getBudget());
        trip.setStatus(dto.getStatus());
        trip.setNotes(dto.getNotes());
        
        return trip;
    }
}
