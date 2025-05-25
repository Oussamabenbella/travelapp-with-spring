package com.example.travelappv2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour rediriger vers la documentation Swagger
 */
@Controller
public class SwaggerController {

    /**
     * Redirige vers l'interface Swagger UI
     */
    @GetMapping("/api-doc")
    public String redirectToSwaggerUi() {
        return "redirect:/swagger-ui/index.html";
    }
}
