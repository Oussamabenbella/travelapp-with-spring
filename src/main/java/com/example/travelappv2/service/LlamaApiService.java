package com.example.travelappv2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Service pour communiquer avec l'API Llama
 */
@Service
public class LlamaApiService {

    @Value("${llama.api.url:https://api.example.com/llama}")
    private String llamaApiUrl;
    
    @Value("${llama.api.key:}")
    private String llamaApiKey;
    
    private final RestTemplate restTemplate;
    
    public LlamaApiService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Génère une réponse via l'API Llama
     * 
     * @param userMessage Le message de l'utilisateur
     * @return La réponse générée par Llama
     */
    public String generateResponse(String userMessage) {
        try {
            // Configuration des headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (llamaApiKey != null && !llamaApiKey.isEmpty()) {
                headers.set("Authorization", "Bearer " + llamaApiKey);
            }
            
            // Préparation du body de la requête
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("prompt", createTravelPrompt(userMessage));
            requestBody.put("max_tokens", 256);
            requestBody.put("temperature", 0.7);
            
            // Création de l'entité HTTP
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // Appel à l'API
            ResponseEntity<Map> response = restTemplate.postForEntity(llamaApiUrl, requestEntity, Map.class);
            
            // Traitement de la réponse
            if (response.getBody() != null && response.getBody().containsKey("text")) {
                return (String) response.getBody().get("text");
            }
            
            return fallbackResponse(userMessage);
        } catch (Exception e) {
            // En cas d'erreur, utiliser la réponse de secours
            return fallbackResponse(userMessage);
        }
    }
    
    /**
     * Crée un prompt formaté pour les questions de voyage
     */
    private String createTravelPrompt(String userMessage) {
        return "Tu es un assistant de voyage professionnel et expert. " +
               "Réponds de manière précise, informative et utile à cette question sur le voyage : " + 
               userMessage;
    }
    
    /**
     * Génère une réponse de secours basée sur des mots-clés
     */
    private String fallbackResponse(String userMessage) {
        String lowerMessage = userMessage.toLowerCase();
        
        if (lowerMessage.contains("paris") || lowerMessage.contains("france") || lowerMessage.contains("eiffel")) {
            return "Paris est magnifique toute l'année, mais le printemps (avril-juin) et l'automne (septembre-octobre) " +
                   "offrent le meilleur équilibre entre affluence et météo. Prévoyez au moins 3 jours pour voir " +
                   "les attractions principales comme la Tour Eiffel, le Louvre et Notre-Dame.";
        }
        
        if (lowerMessage.contains("japon") || lowerMessage.contains("tokyo") || lowerMessage.contains("kyoto")) {
            return "Le Japon est idéal au printemps (mars-mai) pour les cerisiers en fleur et à l'automne (septembre-novembre) " +
                   "pour les couleurs. Pensez à prendre un Japan Rail Pass si vous visitez plusieurs villes. " +
                   "Tokyo offre modernité et technologie, tandis que Kyoto préserve la tradition et les temples.";
        }
        
        if (lowerMessage.contains("italie") || lowerMessage.contains("rome") || lowerMessage.contains("venise")) {
            return "Pour l'Italie, prévoyez environ 100-150€/jour pour l'hébergement, les repas et les attractions. " +
                   "Évitez l'été pour les grandes villes (chaleur et foules). Le printemps et l'automne sont parfaits. " +
                   "Rome, Florence et Venise sont des incontournables, mais ne négligez pas des villes comme Bologne ou Naples.";
        }
        
        if (lowerMessage.contains("sécurité") || lowerMessage.contains("securite") || lowerMessage.contains("danger")) {
            return "Conseils de sécurité en voyage: gardez une copie de vos documents, souscrivez une assurance voyage, " +
                   "renseignez-vous sur les zones à éviter, partagez votre itinéraire avec un proche, et gardez toujours " +
                   "un téléphone chargé avec les numéros d'urgence locaux.";
        }
        
        return "Merci pour votre question sur les voyages ! Pour vous aider au mieux, pourriez-vous me poser " +
               "une question plus spécifique sur une destination particulière, le budget, la planification, " +
               "la météo, les conseils de sécurité ou les voyages en famille ?";
    }
}
