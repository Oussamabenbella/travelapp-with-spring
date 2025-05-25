package com.example.travelappv2.repository;

import com.example.travelappv2.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
}
