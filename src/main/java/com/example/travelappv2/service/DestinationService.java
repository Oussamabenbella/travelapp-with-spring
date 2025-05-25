package com.example.travelappv2.service;

import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }
    
    public Page<Destination> getAllDestinations(Pageable pageable) {
        return destinationRepository.findAll(pageable);
    }

    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    public Destination saveDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    public void deleteDestination(Long id) {
        destinationRepository.deleteById(id);
    }
}
