package com.example.travelappv2.controller.api;

import com.example.travelappv2.service.GroqApiCurlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Contrôleur REST pour l'API du chatbot avec implémentation curl
 */
@CrossOrigin(origins = "*") // Permet les requêtes de toutes origines
@RestController
@RequestMapping("/api/chatbot/groq-curl")
public class GroqCurlChatController {

    private static final Logger LOGGER = Logger.getLogger(GroqCurlChatController.class.getName());
    private final GroqApiCurlService groqApiCurlService;
    
    @Autowired
    public GroqCurlChatController(GroqApiCurlService groqApiCurlService) {
        this.groqApiCurlService = groqApiCurlService;
    }
    
    /**
     * Endpoint pour générer une réponse à partir d'un message utilisateur
     * Utilise une implémentation basée sur curl
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        LOGGER.info("Requête reçue pour le message: " + userMessage);
        
        String response = groqApiCurlService.generateResponse(userMessage);
        LOGGER.info("Réponse générée: " + response);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        
        return ResponseEntity.ok(result);
    }
}
