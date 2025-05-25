package com.example.travelappv2.controller.api;

import com.example.travelappv2.service.GroqApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour l'API du chatbot utilisant Groq Cloud (Llama)
 */
@CrossOrigin(origins = "*") // Permet les requêtes de toutes origines
@RestController
@RequestMapping("/api/chatbot/groq")
@Tag(name = "Groq Chatbot", description = "API pour interagir avec le chatbot Groq utilisant Llama")
public class GroqChatController {

    private final GroqApiService groqApiService;
    
    @Autowired
    public GroqChatController(GroqApiService groqApiService) {
        this.groqApiService = groqApiService;
    }
    
    /**
     * Endpoint pour générer une réponse à partir d'un message utilisateur
     */
    @Operation(summary = "Générer une réponse du chatbot", description = "Envoie un message utilisateur à l'API Groq et retourne une réponse générée")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Réponse générée avec succès",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "500", description = "Erreur lors de la génération de la réponse")
    })
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(
            @Parameter(description = "Message de l'utilisateur", required = true)
            @RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String response = groqApiService.generateResponse(userMessage);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        
        return ResponseEntity.ok(result);
    }
}
