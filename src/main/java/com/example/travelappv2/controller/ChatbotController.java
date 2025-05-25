package com.example.travelappv2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Contrôleur pour le chatbot de voyage simple
 */
@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

    // Map pour stocker les patterns et les réponses correspondantes
    private final Map<Pattern, String> responses = new HashMap<>();
    
    /**
     * Constructeur qui initialise les réponses du chatbot
     */
    public ChatbotController() {
        initializeResponses();
    }
    
    /**
     * Initialise les patterns et réponses du chatbot
     */
    private void initializeResponses() {
        // Questions générales
        responses.put(Pattern.compile("(?i).*bonjour.*|.*salut.*|.*hello.*"), 
                "Bonjour ! Je suis votre assistant de voyage virtuel. Comment puis-je vous aider aujourd'hui ?");
        
        responses.put(Pattern.compile("(?i).*au revoir.*|.*adieu.*|.*bye.*"), 
                "Merci d'avoir utilisé notre assistant de voyage. Au plaisir de vous aider pour votre prochain voyage !");
        
        // Questions sur les destinations
        responses.put(Pattern.compile("(?i).*paris.*|.*france.*"), 
                "Paris est une destination magnifique ! Vous pourriez visiter la Tour Eiffel, le Louvre, Notre-Dame, le Sacré-Cœur, et les Champs-Élysées. " +
                "La gastronomie française est également exceptionnelle avec ses croissants, fromages, et vins. " +
                "La meilleure période pour visiter est entre avril et juin ou entre septembre et octobre pour éviter les foules estivales.");
        
        responses.put(Pattern.compile("(?i).*rome.*|.*italie.*"), 
                "L'Italie est une destination riche en histoire, culture et gastronomie ! Visitez Rome pour le Colisée et le Vatican, " +
                "Florence pour ses musées et la Renaissance, Venise pour ses canaux uniques, et la Toscane pour ses paysages et vins. " +
                "La cuisine italienne varie selon les régions : pâtes, pizzas, risottos, gelato, et bien plus encore. " +
                "Visitez au printemps (avril-mai) ou en automne (septembre-octobre) pour éviter les foules estivales et la chaleur intense.");
        
        responses.put(Pattern.compile("(?i).*japon.*|.*tokyo.*|.*kyoto.*"), 
                "Le Japon offre un mélange fascinant entre tradition et modernité ! À Tokyo, visitez le quartier électronique d'Akihabara, " +
                "le sanctuaire Meiji et la tour Tokyo Skytree. Kyoto vous enchantera avec ses temples traditionnels comme le Kinkaku-ji (Pavillon d'or) " +
                "et ses jardins zen. Goûtez aux spécialités culinaires comme les sushis, les ramens et les tempuras. Les meilleures périodes " +
                "sont le printemps pour les cerisiers en fleur (mars-avril) et l'automne pour les couleurs d'automne (octobre-novembre).");
        
        responses.put(Pattern.compile("(?i).*new york.*|.*états-unis.*|.*etats-unis.*|.*usa.*"), 
                "New York est une ville vibrante et cosmopolite ! Ne manquez pas l'Empire State Building, Times Square, Central Park, " +
                "le MoMA et la Statue de la Liberté. Profitez de la diversité culinaire et des spectacles de Broadway. " +
                "Visitez au printemps (avril-juin) ou en automne (septembre-novembre) pour un climat agréable.");
                
        responses.put(Pattern.compile("(?i).*barcelone.*|.*espagne.*"), 
                "Barcelone est une ville dynamique avec une architecture unique ! Découvrez les œuvres de Gaudí comme la Sagrada Familia, " +
                "le Parc Güell et Casa Batlló. Promenez-vous sur Las Ramblas et détendez-vous sur les plages de la Barceloneta. " +
                "La gastronomie catalane est délicieuse, essayez les tapas et la paella. Visitez de mai à juin ou de septembre à octobre pour un climat idéal.");
        
        // Questions sur le budget
        responses.put(Pattern.compile("(?i).*budget.*|.*coût.*|.*cout.*|.*prix.*|.*cher.*"), 
                "Le budget voyage varie considérablement selon plusieurs facteurs :\n\n" +
                "Par région du monde (budget quotidien moyen par personne) :\n" +
                "- Asie du Sud-Est : 30-70€ (petit budget), 70-120€ (confort)\n" +
                "- Europe de l'Est : 50-80€ (petit budget), 80-150€ (confort)\n" +
                "- Europe de l'Ouest : 80-120€ (petit budget), 120-250€ (confort)\n" +
                "- Amérique du Nord : 100-150€ (petit budget), 150-300€ (confort)\n" +
                "- Japon/Australie : 100-150€ (petit budget), 150-300€ (confort)\n\n" +
                "Répartition typique :\n" +
                "- Hébergement : 30-40% du budget\n" +
                "- Nourriture : 20-25%\n" +
                "- Transport local : 15-20%\n" +
                "- Activités et visites : 10-15%\n" +
                "- Divers (souvenirs, achats) : 5-10%");
        
        // Questions sur la planification
        responses.put(Pattern.compile("(?i).*planifier.*|.*organiser.*|.*préparation.*|.*preparation.*|.*planification.*"), 
                "Pour bien planifier votre voyage, voici une méthode en plusieurs étapes :\n\n" +
                "1) Recherche et inspiration : Définissez le type d'expérience que vous recherchez (détente, aventure, culture) et choisissez une destination qui correspond.\n" +
                "2) Budget et durée : Établissez votre budget total et déterminez la durée idéale en fonction de vos contraintes.\n" +
                "3) Documentation : Vérifiez les exigences en matière de passeport et de visa. Renseignez-vous sur les vaccins recommandés.\n" +
                "4) Transport : Réservez vos billets d'avion idéalement 2-3 mois à l'avance pour les meilleurs prix.\n" +
                "5) Hébergement : Choisissez selon votre style de voyage et votre budget (hôtel, auberge, location, etc.).\n" +
                "6) Itinéraire : Planifiez un itinéraire réaliste avec des temps de repos.\n" +
                "7) Assurance : Souscrivez à une assurance voyage adaptée à vos besoins.\n" +
                "8) Check-list finale : Préparez vos bagages, téléchargez les applications utiles, et informez votre banque de vos déplacements.");
        
        // Questions sur la météo
        responses.put(Pattern.compile("(?i).*météo.*|.*meteo.*|.*climat.*|.*saison.*|.*quand visiter.*|.*période.*|.*periode.*"), 
                "La météo est un facteur essentiel pour réussir votre voyage. Voici un guide par région :\n\n" +
                "Europe :\n" +
                "- Méditerranée : Mai-Juin et Septembre-Octobre sont idéaux (chaud mais pas caniculaire, moins de touristes)\n" +
                "- Europe du Nord : Juin-Août offrent les meilleures conditions (températures douces, longues journées)\n" +
                "- Europe Centrale : Mai-Septembre pour un temps agréable (avec juillet-août parfois chaud et bondé)\n\n" +
                "Asie :\n" +
                "- Asie du Sud-Est : Novembre-Février (saison sèche), évitez Mai-Octobre (mousson)\n" +
                "- Japon : Mars-Avril (cerisiers en fleur) et Octobre-Novembre (couleurs d'automne)\n" +
                "- Inde : Octobre-Mars (évitez la mousson de Juin-Septembre et la chaleur d'Avril-Mai)\n\n" +
                "Amérique :\n" +
                "- USA/Canada : Mai-Juin et Septembre-Octobre pour la plupart des régions\n" +
                "- Mexique/Caraïbes : Décembre-Avril (saison sèche), évitez Septembre-Octobre (ouragans)\n" +
                "- Amérique du Sud : Variable selon la région (le Brésil est idéal d'Avril à Octobre)");
        
        // Questions sur la sécurité
        responses.put(Pattern.compile("(?i).*sécurité.*|.*securite.*|.*danger.*|.*risque.*|.*vol.*"), 
                "La sécurité est primordiale en voyage. Voici des conseils essentiels :\n\n" +
                "Avant de partir :\n" +
                "- Consultez les conseils aux voyageurs officiels de votre gouvernement\n" +
                "- Souscrivez une assurance voyage complète (médicale, rapatriement, annulation)\n" +
                "- Enregistrez-vous auprès de votre ambassade si vous voyagez dans une zone à risque\n" +
                "- Faites des copies numériques de vos documents importants (passeport, assurance, billets)\n\n" +
                "Sécurité personnelle :\n" +
                "- Restez vigilant dans les zones touristiques et transports en commun (pickpockets)\n" +
                "- Utilisez un portefeuille discret sous vos vêtements pour l'argent et les documents importants\n" +
                "- Évitez d'afficher des objets de valeur (bijoux, appareils électroniques coûteux)\n" +
                "- Méfiez-vous des arnaques courantes ciblant les touristes");
        
        // Questions sur les voyages en famille
        responses.put(Pattern.compile("(?i).*famille.*|.*enfant.*|.*bébé.*|.*bebe.*"), 
                "Pour un voyage réussi en famille, voici quelques recommandations :\n\n" +
                "Destinations idéales pour les familles :\n" +
                "- Espagne : Côtes ensoleillées, parcs à thème et villes accueillantes comme Barcelone\n" +
                "- Royaume-Uni : Londres avec ses musées gratuits et nombreuses activités pour enfants\n" +
                "- Thaïlande : Plages sécuritaires, population accueillante envers les enfants\n" +
                "- Costa Rica : Écotourisme, observation de la faune et activités d'aventure adaptées\n\n" +
                "Conseils pratiques :\n" +
                "- Prévoyez des trajets plus courts et des pauses régulières\n" +
                "- Réservez des hébergements adaptés aux familles avec cuisinette ou pension\n" +
                "- Emportez des divertissements pour les temps d'attente et les trajets\n" +
                "- Alternez les activités pour plaire à tous les âges\n" +
                "- Gardez une routine minimale pour les repas et le sommeil");
    }
    
    /**
     * Affiche la page du chatbot
     */
    @GetMapping
    public String chatbotPage(Model model) {
        model.addAttribute("title", "Assistant de Voyage | Travel With Us");
        return "chatbot-static";
    }
    
    /**
     * Affiche la page du chatbot avec Llama
     */
    @GetMapping("/llama")
    public String llamaChatbotPage(Model model) {
        model.addAttribute("title", "Assistant de Voyage Llama | Travel With Us");
        return "redirect:/chatbot-llama.html";
    }
    
    /**
     * Affiche la page du chatbot avec Groq (Llama 4)
     */
    @GetMapping("/groq")
    public String groqChatbotPage(Model model) {
        model.addAttribute("title", "Assistant de Voyage Llama via Groq | Travel With Us");
        return "redirect:/chatbot-direct.html";
    }
    
    /**
     * Traite les requêtes du chatbot et renvoie une réponse
     */
    @PostMapping("/ask")
    @ResponseBody
    public Map<String, Object> processMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String response = generateResponse(userMessage);
        
        Map<String, Object> result = new HashMap<>();
        result.put("response", response);
        return result;
    }
    
    /**
     * Génère une réponse basée sur le message de l'utilisateur
     */
    private String generateResponse(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Bonjour, comment puis-je vous aider à planifier votre voyage ?";
        }
        
        for (Map.Entry<Pattern, String> entry : responses.entrySet()) {
            if (entry.getKey().matcher(userMessage).matches()) {
                return entry.getValue();
            }
        }
        
        // Réponse par défaut si aucun pattern ne correspond
        return "Je ne suis pas sûr de comprendre votre question. Pourriez-vous me demander des informations sur une destination spécifique (comme Paris, Rome, Japon), " +
               "sur la planification de voyage, le budget, la météo, ou la sécurité ?";
    }
}
