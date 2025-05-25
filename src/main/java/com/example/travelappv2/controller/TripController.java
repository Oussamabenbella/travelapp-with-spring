package com.example.travelappv2.controller;

import com.example.travelappv2.entity.Attraction;
import com.example.travelappv2.entity.Trip;
import com.example.travelappv2.service.AttractionService;
import com.example.travelappv2.service.DestinationService;
import com.example.travelappv2.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;
    
    @Autowired
    private DestinationService destinationService;
    
    @Autowired
    private AttractionService attractionService;

    @GetMapping
    public String listTrips(Model model) {
        model.addAttribute("trips", tripService.getAllTrips());
        return "trips/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Trip trip = new Trip();
        // Définir un statut par défaut pour les nouveaux voyages
        trip.setStatus("PLANNING");
        
        model.addAttribute("trip", trip);
        model.addAttribute("destinations", destinationService.getAllDestinations());
        return "trips/form";
    }

    @PostMapping("/save")
    public String saveTrip(@ModelAttribute("trip") Trip trip, RedirectAttributes redirectAttributes) {
        boolean isNew = (trip.getId() == null);
        tripService.saveTrip(trip);
        
        if (isNew) {
            redirectAttributes.addFlashAttribute("success", "Votre voyage a été créé avec succès !");
        } else {
            redirectAttributes.addFlashAttribute("success", "Les modifications ont été enregistrées avec succès !");
        }
        
        return "redirect:/trips";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Trip trip = tripService.getTripById(id).orElse(null);
        
        if (trip == null) {
            redirectAttributes.addFlashAttribute("error", "Ce voyage n'existe pas ou a été supprimé.");
            return "redirect:/trips";
        }
        
        model.addAttribute("trip", trip);
        model.addAttribute("destinations", destinationService.getAllDestinations());
        return "trips/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrip(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tripService.deleteTrip(id);
            redirectAttributes.addFlashAttribute("success", "Le voyage a été supprimé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de la suppression du voyage.");
        }
        return "redirect:/trips";
    }
    
    @GetMapping("/view/{id}")
    public String viewTrip(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Trip trip = tripService.getTripById(id).orElse(null);
        
        if (trip == null) {
            redirectAttributes.addFlashAttribute("error", "Ce voyage n'existe pas ou a été supprimé.");
            return "redirect:/trips";
        }
        
        model.addAttribute("trip", trip);
        return "trips/view";
    }
    
    @GetMapping("/{tripId}/add-attraction")
    public String showAddAttractionForm(@PathVariable Long tripId, Model model, RedirectAttributes redirectAttributes) {
        Trip trip = tripService.getTripById(tripId).orElse(null);
        
        if (trip == null) {
            redirectAttributes.addFlashAttribute("error", "Ce voyage n'existe pas ou a été supprimé.");
            return "redirect:/trips";
        }
        
        // Si une destination est définie, on récupère les attractions de cette destination
        if (trip.getDestination() != null) {
            model.addAttribute("availableAttractions", attractionService.getAttractionsByDestination(trip.getDestination().getId()));
        } else {
            // Sinon on récupère toutes les attractions
            model.addAttribute("availableAttractions", attractionService.getAllAttractions());
        }
        
        model.addAttribute("trip", trip);
        return "trips/add-attraction";
    }
    
    @PostMapping("/{tripId}/add-attraction")
    public String addAttractionToTrip(@PathVariable Long tripId, @RequestParam Long attractionId, RedirectAttributes redirectAttributes) {
        try {
            Trip trip = tripService.getTripById(tripId).orElseThrow(() -> new Exception("Voyage non trouvé"));
            Attraction attraction = attractionService.getAttractionById(attractionId).orElseThrow(() -> new Exception("Attraction non trouvée"));
            
            trip.addAttraction(attraction);
            tripService.saveTrip(trip);
            
            redirectAttributes.addFlashAttribute("success", "L'attraction a été ajoutée à votre voyage avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de l'ajout de l'attraction: " + e.getMessage());
        }
        
        return "redirect:/trips/edit/" + tripId;
    }
    
    @GetMapping("/{tripId}/remove-attraction/{attractionId}")
    public String removeAttractionFromTrip(@PathVariable Long tripId, @PathVariable Long attractionId, RedirectAttributes redirectAttributes) {
        try {
            Trip trip = tripService.getTripById(tripId).orElseThrow(() -> new Exception("Voyage non trouvé"));
            Attraction attraction = attractionService.getAttractionById(attractionId).orElseThrow(() -> new Exception("Attraction non trouvée"));
            
            trip.removeAttraction(attraction);
            tripService.saveTrip(trip);
            
            redirectAttributes.addFlashAttribute("success", "L'attraction a été retirée de votre voyage.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors du retrait de l'attraction: " + e.getMessage());
        }
        
        return "redirect:/trips/edit/" + tripId;
    }
}
