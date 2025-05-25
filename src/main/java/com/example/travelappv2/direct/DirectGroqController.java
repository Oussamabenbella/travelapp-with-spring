package com.example.travelappv2.direct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur ultra-simplifié pour l'API Groq sans dépendances
 */
@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Direct Groq API", description = "API directe pour Groq - implémentation de type curl")
public class DirectGroqController {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "meta-llama/llama-4-scout-17b-16e-instruct";
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Point d'entrée simple pour le chat
     */
    @Operation(summary = "Chat avec Groq (implémentation directe)", 
               description = "Envoie un message à l'API Groq en utilisant une implémentation directe similaire à curl")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Réponse générée avec succès",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "500", description = "Erreur lors de la communication avec l'API Groq")
    })
    @PostMapping("/direct-groq-chat")
    public Map<String, String> chat(
            @Parameter(description = "Message de l'utilisateur", required = true)
            @RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            String userMessage = request.get("message");
            if (userMessage == null || userMessage.trim().isEmpty()) {
                response.put("response", "Veuillez poser une question.");
                return response;
            }
            
            String result = callGroqApi(userMessage);
            response.put("response", result);
            
        } catch (Exception e) {
            response.put("response", "Erreur lors du traitement de la demande: " + e.getMessage());
            response.put("error", e.toString());
        }
        return response;
    }
    
    /**
     * Appel direct à l'API Groq en utilisant HttpURLConnection
     */
    private String callGroqApi(String userMessage) {
        try {
            // Préparer la connexion HTTP
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(20000);
            
            // Construire le payload JSON exactement comme dans la commande curl
            String payload = String.format(
                "{\n" +
                "\"model\": \"%s\",\n" +
                "\"messages\": [{\n" +
                "    \"role\": \"system\",\n" +
                "    \"content\": \"Tu es un assistant de voyage professionnel et expert. Réponds de manière précise, informative et utile aux questions sur le voyage. Concentre-toi sur les aspects pratiques: destinations, budgets, attractions, conseils culturels.\"\n" +
                "}, {\n" +
                "    \"role\": \"user\",\n" +
                "    \"content\": \"%s\"\n" +
                "}]\n" +
                "}", MODEL, userMessage.replace("\"", "\\\""));
            
            // Envoyer la requête
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Obtenir la réponse
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    String responseStr = response.toString();
                    
                    // Extraire le contenu de la réponse
                    JsonNode jsonNode = objectMapper.readTree(responseStr);
                    if (jsonNode.has("choices") && jsonNode.get("choices").size() > 0) {
                        JsonNode content = jsonNode.get("choices").get(0).get("message").get("content");
                        if (content != null) {
                            return content.asText();
                        }
                    }
                    
                    return "Erreur dans le format de la réponse: " + responseStr;
                }
            } else {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return "Erreur lors de l'appel à l'API Groq. Code: " + responseCode + ", Réponse: " + response.toString();
                }
            }
            
        } catch (Exception e) {
            return "Erreur technique: " + e.getMessage();
        }
    }
}
