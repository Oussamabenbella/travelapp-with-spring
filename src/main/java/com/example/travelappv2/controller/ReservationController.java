package com.example.travelappv2.controller;

import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.entity.Reservation;
import com.example.travelappv2.service.DestinationService;
import com.example.travelappv2.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private DestinationService destinationService;
    
    /**
     * Afficher toutes les réservations (Admin uniquement)
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        Page<Reservation> reservationPage = reservationService.getAllReservations(
                PageRequest.of(page, size, Sort.by("startDate").descending()));
        
        model.addAttribute("reservationPage", reservationPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservationPage.getTotalPages());
        model.addAttribute("title", "Toutes les réservations");
        
        return "reservations/list";
    }
    
    /**
     * Afficher mes réservations (Utilisateur connecté)
     */
    @GetMapping("/my")
    public String getMyReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        Page<Reservation> reservationPage = reservationService.getMyReservations(
                PageRequest.of(page, size, Sort.by("startDate").descending()));
        
        model.addAttribute("reservationPage", reservationPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservationPage.getTotalPages());
        model.addAttribute("title", "Mes réservations");
        
        return "reservations/my-list";
    }
    
    /**
     * Afficher le formulaire pour réserver une destination
     */
    @GetMapping("/book/{destinationId}")
    public String showBookingForm(@PathVariable Long destinationId, Model model) {
        Destination destination = destinationService.getDestinationById(destinationId)
            .orElseThrow(() -> new RuntimeException("Destination non trouvée avec l'ID : " + destinationId));
        
        Reservation reservation = new Reservation();
        reservation.setDestination(destination);
        reservation.setStartDate(LocalDate.now().plusDays(1));
        reservation.setEndDate(LocalDate.now().plusDays(8));
        reservation.setNumberOfPeople(1);
        
        model.addAttribute("reservation", reservation);
        model.addAttribute("destination", destination);
        model.addAttribute("title", "Réserver " + destination.getName());
        
        return "reservations/booking-form";
    }
    
    /**
     * Traiter la soumission du formulaire de réservation
     */
    @PostMapping("/book/{destinationId}")
    public String processBooking(
            @PathVariable Long destinationId,
            @ModelAttribute("reservation") @Valid Reservation reservationForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            Destination destination = destinationService.getDestinationById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination non trouvée avec l'ID : " + destinationId));
            model.addAttribute("destination", destination);
            model.addAttribute("title", "Réserver " + destination.getName());
            return "reservations/booking-form";
        }
        
        try {
            Reservation reservation = reservationService.createReservation(
                destinationId,
                reservationForm.getStartDate(),
                reservationForm.getEndDate(),
                reservationForm.getNumberOfPeople(),
                reservationForm.getNotes()
            );
            
            redirectAttributes.addFlashAttribute("success", 
                "Votre réservation a été enregistrée avec succès et est en attente de confirmation.");
            return "redirect:/reservations/my";
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            Destination destination = destinationService.getDestinationById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination non trouvée avec l'ID : " + destinationId));
            model.addAttribute("destination", destination);
            model.addAttribute("title", "Réserver " + destination.getName());
            return "reservations/booking-form";
        }
    }
    
    /**
     * Afficher les détails d'une réservation
     */
    @GetMapping("/{id}")
    public String getReservationDetails(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("title", "Détails de la réservation");
        return "reservations/details";
    }
    
    /**
     * Afficher le formulaire de modification d'une réservation
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("destination", reservation.getDestination());
        model.addAttribute("title", "Modifier la réservation");
        return "reservations/edit-form";
    }
    
    /**
     * Traiter la soumission du formulaire de modification
     */
    @PostMapping("/edit/{id}")
    public String processEdit(
            @PathVariable Long id,
            @ModelAttribute("reservation") @Valid Reservation reservationForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            Reservation reservation = reservationService.getReservationById(id);
            model.addAttribute("destination", reservation.getDestination());
            model.addAttribute("title", "Modifier la réservation");
            return "reservations/edit-form";
        }
        
        try {
            Reservation reservation = reservationService.updateReservation(
                id,
                reservationForm.getStartDate(),
                reservationForm.getEndDate(),
                reservationForm.getNumberOfPeople(),
                reservationForm.getNotes()
            );
            
            redirectAttributes.addFlashAttribute("success", "Votre réservation a été modifiée avec succès.");
            return "redirect:/reservations/my";
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            Reservation reservation = reservationService.getReservationById(id);
            model.addAttribute("destination", reservation.getDestination());
            model.addAttribute("title", "Modifier la réservation");
            return "reservations/edit-form";
        }
    }
    
    /**
     * Confirmer une réservation (Admin uniquement)
     */
    @PostMapping("/manage/confirm/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String confirmReservation(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            reservationService.confirmReservation(id);
            redirectAttributes.addFlashAttribute("success", "Réservation confirmée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/reservations/all";
    }
    
    /**
     * Annuler une réservation
     */
    @PostMapping("/cancel/{id}")
    public String cancelReservation(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            reservationService.cancelReservation(id);
            redirectAttributes.addFlashAttribute("success", "Réservation annulée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/reservations/my";
    }
    
    /**
     * Vérifier la disponibilité d'une destination (AJAX)
     */
    @GetMapping("/check-availability")
    @ResponseBody
    public boolean checkAvailability(
            @RequestParam Long destinationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return reservationService.isDestinationAvailable(destinationId, startDate, endDate);
    }
}
