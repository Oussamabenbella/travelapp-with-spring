package com.example.travelappv2.service;

import com.example.travelappv2.entity.Attraction;
import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.repository.AttractionRepository;
import com.example.travelappv2.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AttractionService {

    @Autowired
    private AttractionRepository attractionRepository;
    
    @Autowired
    private DestinationRepository destinationRepository;

    public List<Attraction> getAllAttractions() {
        return attractionRepository.findAll();
    }

    public Optional<Attraction> getAttractionById(Long id) {
        return attractionRepository.findById(id);
    }

    public Attraction saveAttraction(Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    public void deleteAttraction(Long id) {
        attractionRepository.deleteById(id);
    }
    
    // Nouvelles méthodes pour utiliser les fonctionnalités avancées
    
    /**
     * Récupère toutes les attractions pour une destination donnée
     */
    public List<Attraction> getAttractionsByDestinationId(Long destinationId) {
        return attractionRepository.findByDestinationId(destinationId);
    }
    
    /**
     * Récupère les attractions les mieux notées pour une destination
     */
    public List<Attraction> getTopAttractionsByDestinationId(Long destinationId) {
        return attractionRepository.findTopAttractionsByDestinationId(destinationId);
    }
    
    /**
     * Ajoute une attraction à une destination existante
     */
    public Attraction addAttractionToDestination(Long destinationId, Attraction attraction) {
        return destinationRepository.findById(destinationId).map(destination -> {
            attraction.setDestination(destination);
            return attractionRepository.save(attraction);
        }).orElse(null);
    }
    
    /**
     * Recherche des attractions par nom (recherche partielle insensible à la casse)
     */
    public List<Attraction> searchAttractionsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return attractionRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Alias pour getAttractionsByDestinationId pour compatibilité avec le TripController
     */
    public List<Attraction> getAttractionsByDestination(Long destinationId) {
        return getAttractionsByDestinationId(destinationId);
    }
}
