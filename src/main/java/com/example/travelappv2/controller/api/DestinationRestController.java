package com.example.travelappv2.controller.api;

import com.example.travelappv2.dto.DestinationDTO;
import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.exception.ResourceNotFoundException;
import com.example.travelappv2.service.DestinationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/destinations")
@Tag(name = "Destination", description = "API de gestion des destinations")
public class DestinationRestController {

    @Autowired
    private DestinationService destinationService;

    @Operation(summary = "Récupérer toutes les destinations", description = "Récupère la liste paginée des destinations avec options de tri")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des destinations récupérée avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Paramètres de requête invalides"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDestinations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        Page<Destination> pageDestinations = destinationService.getAllDestinations(pageable);
        
        List<DestinationDTO> destinationDTOs = pageDestinations.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", destinationDTOs);
        response.put("currentPage", pageDestinations.getNumber());
        response.put("totalItems", pageDestinations.getTotalElements());
        response.put("totalPages", pageDestinations.getTotalPages());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationDTO> getDestinationById(@PathVariable Long id) {
        Destination destination = destinationService.getDestinationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", id));
        
        return new ResponseEntity<>(convertToDTO(destination), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DestinationDTO> createDestination(@Valid @RequestBody DestinationDTO destinationDTO) {
        Destination destination = convertToEntity(destinationDTO);
        Destination savedDestination = destinationService.saveDestination(destination);
        
        return new ResponseEntity<>(convertToDTO(savedDestination), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinationDTO> updateDestination(@PathVariable Long id, @Valid @RequestBody DestinationDTO destinationDTO) {
        Destination destination = destinationService.getDestinationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", id));
        
        destination.setName(destinationDTO.getName());
        destination.setCountry(destinationDTO.getCountry());
        destination.setDescription(destinationDTO.getDescription());
        destination.setLatitude(destinationDTO.getLatitude());
        destination.setLongitude(destinationDTO.getLongitude());
        
        Destination updatedDestination = destinationService.saveDestination(destination);
        
        return new ResponseEntity<>(convertToDTO(updatedDestination), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        Destination destination = destinationService.getDestinationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", id));
        
        destinationService.deleteDestination(id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private DestinationDTO convertToDTO(Destination destination) {
        DestinationDTO dto = new DestinationDTO();
        dto.setId(destination.getId());
        dto.setName(destination.getName());
        dto.setCountry(destination.getCountry());
        dto.setDescription(destination.getDescription());
        dto.setLatitude(destination.getLatitude());
        dto.setLongitude(destination.getLongitude());
        return dto;
    }

    private Destination convertToEntity(DestinationDTO dto) {
        Destination destination = new Destination();
        destination.setId(dto.getId());
        destination.setName(dto.getName());
        destination.setCountry(dto.getCountry());
        destination.setDescription(dto.getDescription());
        destination.setLatitude(dto.getLatitude());
        destination.setLongitude(dto.getLongitude());
        return destination;
    }
}
