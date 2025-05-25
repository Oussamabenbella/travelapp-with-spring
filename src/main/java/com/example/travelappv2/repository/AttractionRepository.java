package com.example.travelappv2.repository;

import com.example.travelappv2.entity.Attraction;
import com.example.travelappv2.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    
    // Trouver toutes les attractions pour une destination spécifique
    List<Attraction> findByDestinationId(Long destinationId);
    
    // Trouver les attractions les mieux notées (top N) pour une destination
    @Query("SELECT a FROM Attraction a WHERE a.destination.id = ?1 ORDER BY a.rating DESC")
    List<Attraction> findTopAttractionsByDestinationId(Long destinationId);
    
    // Trouver par destination
    List<Attraction> findByDestination(Destination destination);
    
    // Recherche d'attractions par nom (contenant une chaîne de caractères)
    List<Attraction> findByNameContainingIgnoreCase(String name);
}
