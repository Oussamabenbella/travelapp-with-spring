package com.example.travelappv2.repository;

import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.entity.Reservation;
import com.example.travelappv2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Trouver toutes les réservations pour un utilisateur spécifique
    List<Reservation> findByUser(User user);
    Page<Reservation> findByUser(User user, Pageable pageable);
    
    // Trouver toutes les réservations pour une destination spécifique
    List<Reservation> findByDestination(Destination destination);
    Page<Reservation> findByDestination(Destination destination, Pageable pageable);
    
    // Vérifier les chevauchements de réservations (pour vérifier la disponibilité)
    @Query("SELECT r FROM Reservation r WHERE r.destination = :destination " +
           "AND ((r.startDate BETWEEN :startDate AND :endDate) " +
           "OR (r.endDate BETWEEN :startDate AND :endDate) " +
           "OR (:startDate BETWEEN r.startDate AND r.endDate))")
    List<Reservation> findOverlappingReservations(Destination destination, LocalDate startDate, LocalDate endDate);
    
    // Trouver les réservations par statut de confirmation
    List<Reservation> findByConfirmed(boolean confirmed);
    Page<Reservation> findByConfirmed(boolean confirmed, Pageable pageable);
    
    // Trouver les réservations pour un utilisateur avec filtrage par confirmation
    List<Reservation> findByUserAndConfirmed(User user, boolean confirmed);
    Page<Reservation> findByUserAndConfirmed(User user, boolean confirmed, Pageable pageable);
    
    // Statistiques - compter les réservations par destination
    @Query("SELECT r.destination.id, r.destination.name, COUNT(r.id) FROM Reservation r GROUP BY r.destination.id, r.destination.name")
    List<Object[]> countReservationsByDestination();
    
    // Statistiques - compter les réservations par utilisateur
    @Query("SELECT r.user.id, r.user.username, COUNT(r.id) FROM Reservation r GROUP BY r.user.id, r.user.username")
    List<Object[]> countReservationsByUser();
}
