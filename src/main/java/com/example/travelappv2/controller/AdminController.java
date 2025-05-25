package com.example.travelappv2.controller;

import com.example.travelappv2.entity.User;
import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.entity.Reservation;
import com.example.travelappv2.entity.Attraction;
import com.example.travelappv2.entity.Trip;
import com.example.travelappv2.service.UserService;
import com.example.travelappv2.service.DestinationService;
import com.example.travelappv2.service.ReservationService;
import com.example.travelappv2.service.AttractionService;
import com.example.travelappv2.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@Secured("ROLE_ADMIN")
public class AdminController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DestinationService destinationService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private AttractionService attractionService;
    
    @Autowired
    private TripService tripService;

    /**
     * Affiche le tableau de bord administrateur avec les statistiques principales
     */
    @GetMapping
    public String adminHome(Model model) {
        // Récupération des données pour le tableau de bord
        List<User> users = userService.getAllUsers();
        List<Destination> destinations = destinationService.getAllDestinations();
        List<Reservation> reservations = reservationService.getAllReservations();
        int attractionCount = attractionService.getAllAttractions().size();
        int tripCount = tripService.getAllTrips().size();
        
        // Ajout des données au modèle
        model.addAttribute("users", users);
        model.addAttribute("userCount", users.size());
        model.addAttribute("destinations", destinations);
        model.addAttribute("destinationCount", destinations.size());
        model.addAttribute("reservations", reservations);
        model.addAttribute("reservationCount", reservations.size());
        model.addAttribute("attractionCount", attractionCount);
        model.addAttribute("tripCount", tripCount);
        
        return "admin/dashboard";
    }
    
    /**
     * Affiche la page de gestion des utilisateurs
     */
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }
    
    /**
     * Affiche la page de gestion des réservations
     */
    @GetMapping("/reservations")
    public String manageReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "admin/reservations";
    }
    
    /**
     * Affiche la page de gestion des destinations
     */
    @GetMapping("/destinations")
    public String manageDestinations(Model model) {
        List<Destination> destinations = destinationService.getAllDestinations();
        model.addAttribute("destinations", destinations);
        model.addAttribute("destination", new Destination()); // Pour le formulaire d'ajout
        return "admin/destinations";
    }
    
    /**
     * Affiche les détails d'un utilisateur spécifique
     */
    @GetMapping("/users/view/{id}")
    public String viewUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Utilisateur introuvable");
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/user-details";
    }
    
    /**
     * Affiche le formulaire d'édition d'un utilisateur
     */
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Utilisateur introuvable");
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/user-form";
    }
    
    /**
     * Affiche les détails d'une destination spécifique
     */
    @GetMapping("/destinations/view/{id}")
    public String viewDestination(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Destination destination = destinationService.getDestinationById(id).orElse(null);
        if (destination == null) {
            redirectAttributes.addFlashAttribute("error", "Destination introuvable");
            return "redirect:/admin/destinations";
        }
        model.addAttribute("destination", destination);
        return "admin/destination-details";
    }
    
    /**
     * Affiche le formulaire d'édition d'une destination
     */
    @GetMapping("/destinations/edit/{id}")
    public String editDestination(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Destination destination = destinationService.getDestinationById(id).orElse(null);
        if (destination == null) {
            redirectAttributes.addFlashAttribute("error", "Destination introuvable");
            return "redirect:/admin/destinations";
        }
        model.addAttribute("destination", destination);
        return "admin/destination-form";
    }
    
    /**
     * Traite l'ajout d'une nouvelle destination
     */
    @PostMapping("/destinations/add")
    public String addDestination(@ModelAttribute Destination destination, RedirectAttributes redirectAttributes) {
        try {
            destinationService.saveDestination(destination);
            redirectAttributes.addFlashAttribute("success", "Destination ajoutée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de la destination: " + e.getMessage());
        }
        return "redirect:/admin/destinations";
    }
    
    /**
     * Supprime une destination
     */
    @GetMapping("/destinations/delete/{id}")
    public String deleteDestination(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            destinationService.deleteDestination(id);
            redirectAttributes.addFlashAttribute("success", "Destination supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/admin/destinations";
    }
    
    /**
     * Affiche la page de gestion des attractions
     */
    @GetMapping("/attractions")
    public String manageAttractions(Model model) {
        List<Attraction> attractions = attractionService.getAllAttractions();
        List<Destination> destinations = destinationService.getAllDestinations();
        model.addAttribute("attractions", attractions);
        model.addAttribute("destinations", destinations);
        model.addAttribute("attraction", new Attraction()); // Pour le formulaire d'ajout
        return "admin/attractions";
    }
    
    /**
     * Affiche la page de gestion des voyages
     */
    @GetMapping("/trips")
    public String manageTrips(Model model) {
        List<Trip> trips = tripService.getAllTrips();
        model.addAttribute("trips", trips);
        return "admin/trips";
    }
}
