package com.example.travelappv2.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service qui implémente exactement la commande curl fournie par l'utilisateur
 */
@Service
public class GroqApiCurlService {
    private static final Logger LOGGER = Logger.getLogger(GroqApiCurlService.class.getName());
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "meta-llama/llama-4-scout-17b-16e-instruct";
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Génère une réponse en utilisant exactement le format de la commande curl fournie
     */
    public String generateResponse(String userMessage) {
        try {
            LOGGER.info("Appel de l'API Groq avec la commande curl équivalente en Java");
            
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
            
            LOGGER.info("Payload: " + payload);
            
            // Envoyer la requête
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Obtenir la réponse
            int responseCode = connection.getResponseCode();
            LOGGER.info("Code de réponse HTTP: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    String responseStr = response.toString();
                    LOGGER.info("Réponse brute: " + responseStr);
                    
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
                    LOGGER.severe("Réponse d'erreur: " + response.toString());
                    return "Erreur lors de l'appel à l'API Groq. Code: " + responseCode + ", Réponse: " + response.toString();
                }
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de l'appel API", e);
            return "Erreur technique: " + e.getMessage();
        }
    }
}
