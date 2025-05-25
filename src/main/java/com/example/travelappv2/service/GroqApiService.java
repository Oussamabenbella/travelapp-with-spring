package com.example.travelappv2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service pour interagir avec l'API Groq Cloud (Llama)
 */
@Service
public class GroqApiService {

    private static final Logger LOGGER = Logger.getLogger(GroqApiService.class.getName());

    private final RestTemplate restTemplate;
    
    @Value("${groq.api.url}")
    private String groqApiUrl;
    
    @Value("${groq.api.key}")
    private String groqApiKey;
    
    @Value("${groq.api.model}")
    private String groqModel;

    public GroqApiService(RestTemplate groqRestTemplate) {
        this.restTemplate = groqRestTemplate;
    }

    /**
     * Génère une réponse basée sur le message de l'utilisateur
     *
     * @param userMessage Le message de l'utilisateur
     * @return La réponse générée
     */
    public String generateResponse(String userMessage) {
        try {
            LOGGER.info("Début de l'appel à l'API Groq avec le modèle: " + groqModel);
            
            // Création des en-têtes HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + groqApiKey);

            // Préparation du corps de la requête - format exactement comme curl
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", groqModel);
            
            List<Map<String, String>> messages = new ArrayList<>();
            
            // Message système pour définir le contexte
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "Tu es un assistant de voyage professionnel et expert. " +
                    "Réponds de manière précise, informative et utile aux questions de l'utilisateur sur le voyage. " +
                    "Propose des conseils personnalisés sur les destinations, la planification, le budget, et la sécurité. " +
                    "Sois concis dans tes réponses (maximum 150 mots). N'invente pas de faits et précise si tu n'as pas l'information. " +
                    "Concentre-toi sur les aspects pratiques du voyage: quand partir, coût estimé, attractions principales, conseils culturels. " +
                    "Utilise un ton amical.");
            
            // Message de l'utilisateur
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            
            messages.add(systemMessage);
            messages.add(userMsg);
            
            requestBody.put("messages", messages);
            
            // Journal de la requête pour le débogage
            LOGGER.info("Envoi de la requête à l'API Groq: " + requestBody);

            // Création de l'entité de requête
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Appel à l'API
            ResponseEntity<Map> response = restTemplate.postForEntity(groqApiUrl, requestEntity, Map.class);
            LOGGER.info("Code de statut de la réponse: " + response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                LOGGER.info("Réponse reçue avec succès");
                try {
                    // Extraction de la réponse
                    Map<String, Object> responseBody = response.getBody();
                    LOGGER.info("Corps de la réponse: " + responseBody);
                    
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> firstChoice = choices.get(0);
                        Map<String, String> message = (Map<String, String>) firstChoice.get("message");
                        
                        if (message != null && message.containsKey("content")) {
                            String content = message.get("content");
                            LOGGER.info("Contenu de la réponse extrait avec succès");
                            return content;
                        } else {
                            LOGGER.warning("Le format du message est incorrect: " + message);
                        }
                    } else {
                        LOGGER.warning("Aucun choix trouvé dans la réponse");
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du traitement de la réponse de l'API Groq", e);
                }
            } else {
                LOGGER.warning("La réponse n'est pas réussie ou le corps est nul");
            }

            // Réponse par défaut en cas d'erreur
            return getFallbackResponse(userMessage);

        } catch (RestClientException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'appel à l'API Groq", e);
            return getFallbackResponse(userMessage);
        }
    }
    
    /**
     * Fournit une réponse de secours basée sur des mots-clés
     */
    private String getFallbackResponse(String message) {
        // Conversion en minuscules pour la recherche de mots-clés
        String lowerMessage = message.toLowerCase();
        
        // Base de connaissances simplifiée pour les réponses de secours
        if (containsAny(lowerMessage, "paris", "france", "eiffel")) {
            return "Paris est magnifique toute l'année, mais le printemps (avril-juin) et l'automne (septembre-octobre) offrent le meilleur équilibre entre affluence et météo. Prévoyez au moins 3 jours pour voir les attractions principales comme la Tour Eiffel, le Louvre et Notre-Dame.";
        } else if (containsAny(lowerMessage, "japon", "tokyo", "kyoto")) {
            return "Le Japon est idéal au printemps (mars-mai) pour les cerisiers en fleur et à l'automne (septembre-novembre) pour les couleurs. Pensez à prendre un Japan Rail Pass si vous visitez plusieurs villes. Tokyo offre modernité et technologie, tandis que Kyoto préserve la tradition et les temples.";
        } else if (containsAny(lowerMessage, "italie", "rome", "venise", "florence")) {
            return "Pour l'Italie, prévoyez environ 100-150€/jour pour l'hébergement, les repas et les attractions. Évitez l'été pour les grandes villes (chaleur et foules). Le printemps et l'automne sont parfaits. Rome, Florence et Venise sont des incontournables, mais ne négligez pas des villes comme Bologne ou Naples.";
        } else if (containsAny(lowerMessage, "sécurité", "securite", "danger")) {
            return "Conseils de sécurité en voyage: gardez une copie de vos documents, souscrivez une assurance voyage, renseignez-vous sur les zones à éviter, partagez votre itinéraire avec un proche, et gardez toujours un téléphone chargé avec les numéros d'urgence locaux.";
        } else if (containsAny(lowerMessage, "budget", "coût", "cout", "argent", "économie")) {
            return "Pour optimiser votre budget: voyagez hors saison, réservez à l'avance, utilisez les transports en commun, mangez où les locaux mangent, et utilisez des applications comme TooGoodToGo pour réduire vos dépenses alimentaires.";
        } else if (containsAny(lowerMessage, "famille", "enfant", "enfants", "bébé", "bebe")) {
            return "En voyage en famille: choisissez un hébergement adapté, prévoyez un rythme plus lent, alternez activités pour enfants et adultes, emportez une trousse de premiers soins et une tablette pour les moments d'attente.";
        } else {
            return "Merci pour votre question ! Pour vous aider au mieux, pourriez-vous me poser une question plus spécifique sur les destinations, le budget, la planification, la météo, les conseils de sécurité ou les voyages en famille ?";
        }
    }
    
    /**
     * Vérifie si une chaîne contient l'un des mots-clés donnés
     */
    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
