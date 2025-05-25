package com.example.travelappv2.controller.api;

import com.example.travelappv2.service.LlamaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour l'API du chatbot Llama
 */
@RestController
@RequestMapping("/api/chatbot/llama")
public class LlamaChatController {

    private final LlamaApiService llamaApiService;
    
    @Autowired
    public LlamaChatController(LlamaApiService llamaApiService) {
        this.llamaApiService = llamaApiService;
    }
    
    /**
     * Endpoint pour générer une réponse à partir d'un message utilisateur
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String response = llamaApiService.generateResponse(userMessage);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        
        return ResponseEntity.ok(result);
    }
}
