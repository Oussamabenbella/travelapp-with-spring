package com.example.travelappv2.controller;

import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/destinations")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public String listDestinations(Model model) {
        model.addAttribute("destinations", destinationService.getAllDestinations());
        return "destinations/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("destination", new Destination());
        return "destinations/form";
    }

    @PostMapping("/save")
    public String saveDestination(@ModelAttribute("destination") Destination destination) {
        destinationService.saveDestination(destination);
        return "redirect:/destinations";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("destination", destinationService.getDestinationById(id).orElse(new Destination()));
        return "destinations/form";
    }
    
    @GetMapping("/{id}")
    public String showDestinationDetails(@PathVariable Long id, Model model) {
        Destination destination = destinationService.getDestinationById(id)
                .orElseThrow(() -> new RuntimeException("Destination non trouvée avec l'ID : " + id));
                
        model.addAttribute("destination", destination);
        return "destinations/details";
    }

    @GetMapping("/delete/{id}")
    public String deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return "redirect:/destinations";
    }
}
