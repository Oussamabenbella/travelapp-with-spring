package com.example.travelappv2.controller;

import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private DestinationService destinationService;

    @GetMapping("/")
    public String home(Model model) {
        // Récupérer les destinations populaires (top 3 par note)
        List<Destination> popularDestinations = destinationService.getAllDestinations().stream()
                .sorted((d1, d2) -> d2.getRating() != null && d1.getRating() != null ? 
                        Double.compare(d2.getRating(), d1.getRating()) : 0)
                .limit(3)
                .toList();
        
        model.addAttribute("title", "Accueil - TravelAppV2");
        model.addAttribute("popularDestinations", popularDestinations);
        return "index";
    }
}
