package com.example.travelappv2.controller;

import com.example.travelappv2.entity.Attraction;
import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.service.AttractionService;
import com.example.travelappv2.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;
    
    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public String listAttractions(Model model, @RequestParam(required = false) String query) {
        if (query != null && !query.isEmpty()) {
            model.addAttribute("attractions", attractionService.searchAttractionsByName(query));
            model.addAttribute("searchQuery", query);
        } else {
            model.addAttribute("attractions", attractionService.getAllAttractions());
        }
        return "attractions/list";
    }
    
    @GetMapping("/destination/{destinationId}")
    public String listAttractionsByDestination(@PathVariable Long destinationId, Model model) {
        destinationService.getDestinationById(destinationId).ifPresent(destination -> {
            model.addAttribute("destination", destination);
            model.addAttribute("attractions", attractionService.getAttractionsByDestinationId(destinationId));
        });
        return "attractions/destination-attractions";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, @RequestParam(required = false) Long destinationId) {
        Attraction attraction = new Attraction();
        
        // Si un destinationId est fourni, nous pré-remplissons la destination
        if (destinationId != null) {
            destinationService.getDestinationById(destinationId).ifPresent(destination -> {
                attraction.setDestination(destination);
                model.addAttribute("destinationName", destination.getName());
            });
        }
        
        model.addAttribute("attraction", attraction);
        model.addAttribute("destinations", destinationService.getAllDestinations());
        return "attractions/form";
    }
    
    @GetMapping("/create/destination/{destinationId}")
    public String showCreateFormForDestination(@PathVariable Long destinationId, Model model) {
        return showCreateForm(model, destinationId);
    }

    @PostMapping("/save")
    public String saveAttraction(
            @ModelAttribute("attraction") Attraction attraction, 
            BindingResult result,
            @RequestParam(required = false) Long destinationId,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "attractions/form";
        }
        
        // Si un ID de destination est fourni, on associe l'attraction à cette destination
        if (destinationId != null) {
            Attraction savedAttraction = attractionService.addAttractionToDestination(destinationId, attraction);
            if (savedAttraction != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Attraction ajoutée avec succès !");
                return "redirect:/attractions/destination/" + destinationId;
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Destination introuvable");
                return "redirect:/attractions";
            }
        } else if (attraction.getDestination() != null && attraction.getDestination().getId() != null) {
            // Si l'attraction a déjà une destination associée
            attractionService.saveAttraction(attraction);
            redirectAttributes.addFlashAttribute("successMessage", "Attraction mise à jour avec succès !");
            return "redirect:/attractions/destination/" + attraction.getDestination().getId();
        } else {
            // Sauvegarde sans destination associée
            attractionService.saveAttraction(attraction);
            redirectAttributes.addFlashAttribute("successMessage", "Attraction créée avec succès !");
            return "redirect:/attractions";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Attraction attraction = attractionService.getAttractionById(id).orElse(null);
        if (attraction == null) {
            return "redirect:/attractions";
        }
        
        model.addAttribute("attraction", attraction);
        model.addAttribute("destinations", destinationService.getAllDestinations());
        if (attraction.getDestination() != null) {
            model.addAttribute("destinationName", attraction.getDestination().getName());
        }
        
        return "attractions/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteAttraction(
            @PathVariable Long id, 
            @RequestParam(required = false) Long destinationId,
            RedirectAttributes redirectAttributes) {
        
        // Récupérer la destination avant de supprimer l'attraction pour rediriger ensuite
        Attraction attraction = attractionService.getAttractionById(id).orElse(null);
        Long redirectDestinationId = destinationId;
        
        if (attraction != null && attraction.getDestination() != null) {
            redirectDestinationId = attraction.getDestination().getId();
        }
        
        attractionService.deleteAttraction(id);
        redirectAttributes.addFlashAttribute("successMessage", "Attraction supprimée avec succès !");
        
        if (redirectDestinationId != null) {
            return "redirect:/attractions/destination/" + redirectDestinationId;
        } else {
            return "redirect:/attractions";
        }
    }
    
    // Méthode pour afficher les détails d'une attraction
    @GetMapping("/{id}")
    public String showAttractionDetails(@PathVariable Long id, Model model) {
        Attraction attraction = attractionService.getAttractionById(id)
                .orElseThrow(() -> new RuntimeException("Attraction non trouvée avec l'ID : " + id));
        
        model.addAttribute("attraction", attraction);
        return "attractions/details";
    }
}
