package com.example.travelappv2.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur d'exemple pour assurer que Swagger détecte correctement les API
 */
@RestController
@RequestMapping("/api/example")
@Tag(name = "Exemple API", description = "API d'exemple pour vérifier que Swagger fonctionne")
public class OpenApiController {

    /**
     * Simple endpoint pour vérifier que Swagger détecte correctement les API
     */
    @Operation(summary = "Récupérer un message de test", 
               description = "Retourne un simple message pour vérifier que Swagger fonctionne")
    @GetMapping("/test")
    public Map<String, String> getTest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "L'API fonctionne correctement");
        return response;
    }
}
