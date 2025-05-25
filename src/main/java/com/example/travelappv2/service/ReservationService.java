package com.example.travelappv2.service;

import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.entity.Reservation;
import com.example.travelappv2.entity.User;
import com.example.travelappv2.repository.DestinationRepository;
import com.example.travelappv2.repository.ReservationRepository;
import com.example.travelappv2.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DestinationRepository destinationRepository;
    
    /**
     * Obtenir toutes les réservations (accessible uniquement aux administrateurs)
     */
    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }
    
    /**
     * Obtenir toutes les réservations sans pagination (pour l'administration)
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    /**
     * Obtenir une réservation par ID avec vérification d'accès
     */
    public Reservation getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Réservation non trouvée avec l'ID : " + id));
        
        // Vérifier que l'utilisateur courant a le droit d'accéder à cette réservation
        if (!canAccessReservation(reservation)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à accéder à cette réservation");
        }
        
        return reservation;
    }
    
    /**
     * Obtenir les réservations de l'utilisateur actuellement connecté
     */
    public Page<Reservation> getMyReservations(Pageable pageable) {
        User currentUser = getCurrentUser();
        return reservationRepository.findByUser(currentUser, pageable);
    }
    
    /**
     * Vérifier si une destination est disponible pour les dates données
     */
    public boolean isDestinationAvailable(Long destinationId, LocalDate startDate, LocalDate endDate) {
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new EntityNotFoundException("Destination non trouvée avec l'ID : " + destinationId));
                
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                destination, startDate, endDate);
                
        return overlappingReservations.isEmpty();
    }
    
    /**
     * Créer une nouvelle réservation
     */
    @Transactional
    public Reservation createReservation(Long destinationId, LocalDate startDate, LocalDate endDate, 
                                        int numberOfPeople, String notes) {
        // Valider les données
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        
        if (LocalDate.now().isAfter(startDate)) {
            throw new IllegalArgumentException("La date de début doit être dans le futur");
        }
        
        if (numberOfPeople <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être supérieur à zéro");
        }
        
        // Vérifier la disponibilité
        if (!isDestinationAvailable(destinationId, startDate, endDate)) {
            throw new IllegalStateException("La destination n'est pas disponible pour les dates sélectionnées");
        }
        
        // Récupérer les entités
        User currentUser = getCurrentUser();
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new EntityNotFoundException("Destination non trouvée avec l'ID : " + destinationId));
        
        // Créer et sauvegarder la réservation
        Reservation reservation = new Reservation();
        reservation.setUser(currentUser);
        reservation.setDestination(destination);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setNotes(notes);
        reservation.setConfirmed(false); // Par défaut, les réservations nécessitent confirmation
        
        return reservationRepository.save(reservation);
    }
    
    /**
     * Mettre à jour une réservation existante
     */
    @Transactional
    public Reservation updateReservation(Long id, LocalDate startDate, LocalDate endDate, 
                                       int numberOfPeople, String notes) {
        // Récupérer la réservation
        Reservation reservation = getReservationById(id);
        
        // Vérifier que l'utilisateur courant a le droit de modifier cette réservation
        if (!canModifyReservation(reservation)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier cette réservation");
        }
        
        // Valider les données
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        
        if (LocalDate.now().isAfter(startDate)) {
            throw new IllegalArgumentException("La date de début doit être dans le futur");
        }
        
        if (numberOfPeople <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être supérieur à zéro");
        }
        
        // Mettre à jour les champs
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setNotes(notes);
        
        // Si admin, pas besoin de réinitialiser le statut de confirmation
        // Si utilisateur standard, la modification réinitialise le statut de confirmation
        if (!isAdmin()) {
            reservation.setConfirmed(false);
        }
        
        return reservationRepository.save(reservation);
    }
    
    /**
     * Confirmer une réservation (administrateur uniquement)
     */
    @Transactional
    public Reservation confirmReservation(Long id) {
        if (!isAdmin()) {
            throw new AccessDeniedException("Seuls les administrateurs peuvent confirmer des réservations");
        }
        
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Réservation non trouvée avec l'ID : " + id));
                
        reservation.setConfirmed(true);
        return reservationRepository.save(reservation);
    }
    
    /**
     * Annuler une réservation
     */
    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = getReservationById(id);
        
        // Vérifier que l'utilisateur courant a le droit d'annuler cette réservation
        if (!canModifyReservation(reservation)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à annuler cette réservation");
        }
        
        reservationRepository.delete(reservation);
    }
    
    /**
     * Vérifier si l'utilisateur courant peut accéder à une réservation
     */
    private boolean canAccessReservation(Reservation reservation) {
        // Les administrateurs peuvent accéder à toutes les réservations
        if (isAdmin()) {
            return true;
        }
        
        // Les utilisateurs standards peuvent accéder à leurs propres réservations
        User currentUser = getCurrentUser();
        return reservation.getUser().getId().equals(currentUser.getId());
    }
    
    /**
     * Vérifier si l'utilisateur courant peut modifier une réservation
     */
    private boolean canModifyReservation(Reservation reservation) {
        // Les administrateurs peuvent modifier toutes les réservations
        if (isAdmin()) {
            return true;
        }
        
        // Les utilisateurs standards peuvent modifier leurs propres réservations non confirmées
        User currentUser = getCurrentUser();
        return reservation.getUser().getId().equals(currentUser.getId());
    }
    
    /**
     * Obtenir l'utilisateur actuellement connecté
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Utilisateur non authentifié"));
    }
    
    /**
     * Vérifier si l'utilisateur courant est un administrateur
     */
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
