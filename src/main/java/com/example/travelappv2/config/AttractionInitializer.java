package com.example.travelappv2.config;

import com.example.travelappv2.entity.Attraction;
import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.repository.AttractionRepository;
import com.example.travelappv2.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(3) // Exécuter après DestinationInitializer qui a l'ordre 2
public class AttractionInitializer implements CommandLineRunner {

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si des attractions existent déjà
        if (attractionRepository.count() > 0) {
            System.out.println("Des attractions existent déjà, initialisation ignorée.");
            return;
        }

        // Récupérer toutes les destinations
        List<Destination> destinations = destinationRepository.findAll();
        if (destinations.isEmpty()) {
            System.out.println("Aucune destination trouvée, initialisation des attractions ignorée.");
            return;
        }

        // Map des attractions par destination
        Map<String, List<Attraction>> attractionsByDestination = new HashMap<>();

        // Paris
        attractionsByDestination.put("Paris", Arrays.asList(
            createAttraction("Tour Eiffel", "La célèbre tour en fer forgé construite en 1889, devenue le symbole de Paris.", "Champ de Mars", 25.50, 5, "https://images.unsplash.com/photo-1543349689-9a4d426bee8e"),
            createAttraction("Musée du Louvre", "Le plus grand musée d'art et d'antiquités au monde, abritant la Joconde et la Vénus de Milo.", "Rue de Rivoli", 17.00, 4, "https://images.unsplash.com/photo-1565099824688-e93eb20fe622"),
            createAttraction("Notre-Dame de Paris", "Magnifique cathédrale gothique du XIIe siècle sur l'île de la Cité, connue pour ses gargouilles et vitraux.", "Île de la Cité", 0.0, 4, "https://images.unsplash.com/photo-1478391679764-b2d8b3cd1e94")
        ));

        // Rome
        attractionsByDestination.put("Rome", Arrays.asList(
            createAttraction("Colisée", "Amphithéâtre antique du Ier siècle, symbole de la Rome impériale où se déroulaient les combats de gladiateurs.", "Piazza del Colosseo", 16.00, 5, "https://images.unsplash.com/photo-1552832230-c0197dd311b5"),
            createAttraction("Fontaine de Trevi", "La plus grande fontaine baroque de Rome, célèbre pour la tradition qui veut qu'on y jette une pièce pour assurer son retour dans la ville.", "Piazza di Trevi", 0.0, 4, "https://images.unsplash.com/photo-1529154691717-3306083d869e"),
            createAttraction("Vatican", "État indépendant au cœur de Rome, abritant la basilique Saint-Pierre et les musées du Vatican avec la chapelle Sixtine.", "Cité du Vatican", 17.00, 5, "https://images.unsplash.com/photo-1531572753322-ad063cecc140")
        ));

        // Barcelone
        attractionsByDestination.put("Barcelone", Arrays.asList(
            createAttraction("Sagrada Familia", "Basilique emblématique conçue par Antoni Gaudí, en construction depuis 1882, célèbre pour son architecture unique.", "Carrer de Mallorca", 26.00, 5, "https://images.unsplash.com/photo-1583779457094-3d9d4d4b3eff"),
            createAttraction("Parc Güell", "Parc public aux formes organiques et mosaïques colorées, autre chef-d'œuvre de Gaudí offrant une vue panoramique sur la ville.", "Carrer d'Olot", 10.00, 4, "https://images.unsplash.com/photo-1551867633-194f125bddfa"),
            createAttraction("La Rambla", "Célèbre avenue piétonne animée de 1,2 km reliant la Place de Catalogne au Vieux Port, bordée de boutiques et de cafés.", "Centre-ville", 0.0, 3, "https://images.unsplash.com/photo-1539037116277-4db20889f2d4")
        ));

        // Tokyo
        attractionsByDestination.put("Tokyo", Arrays.asList(
            createAttraction("Tour de Tokyo", "Tour de communication inspirée de la Tour Eiffel, offrant une vue panoramique sur la ville depuis ses observatoires.", "Minato City", 10.00, 4, "https://images.unsplash.com/photo-1536098561742-ca998e48cbcc"),
            createAttraction("Temple Senso-ji", "Le plus ancien temple bouddhiste de Tokyo fondé en 628, avec sa célèbre porte Kaminarimon et sa rue commerçante Nakamise.", "Asakusa", 0.0, 4, "https://images.unsplash.com/photo-1570041462034-6e5c4e399d90"),
            createAttraction("Shibuya Crossing", "Le carrefour piéton le plus fréquenté au monde, où des milliers de personnes traversent simultanément dans toutes les directions.", "Shibuya", 0.0, 4, "https://images.unsplash.com/photo-1542051841857-5f90071e7989")
        ));

        // New York
        attractionsByDestination.put("New York", Arrays.asList(
            createAttraction("Statue de la Liberté", "Statue monumentale offerte par la France en 1886, symbole de liberté et d'accueil des immigrants aux États-Unis.", "Liberty Island", 18.50, 5, "https://images.unsplash.com/photo-1546436836-07a91091f160"),
            createAttraction("Empire State Building", "Gratte-ciel art déco de 102 étages achevé en 1931, offrant une vue spectaculaire depuis ses observatoires.", "Midtown Manhattan", 42.00, 4, "https://images.unsplash.com/photo-1550308000-07999de621ec"),
            createAttraction("Central Park", "Vaste parc urbain de 341 hectares au cœur de Manhattan, offrant des espaces verts, des lacs et diverses attractions.", "Manhattan", 0.0, 5, "https://images.unsplash.com/photo-1588792806903-aa08c9c56ad5")
        ));

        // Rio de Janeiro
        attractionsByDestination.put("Rio de Janeiro", Arrays.asList(
            createAttraction("Christ Rédempteur", "Statue emblématique du Christ de 30 mètres de haut surplombant la ville depuis le mont Corcovado, l'une des sept nouvelles merveilles du monde.", "Mont Corcovado", 19.00, 5, "https://images.unsplash.com/photo-1518639192441-8fce0a366e2e"),
            createAttraction("Pain de Sucre", "Montagne de granit de 396 mètres accessible par téléphérique, offrant une vue panoramique sur la baie de Guanabara.", "Urca", 23.00, 4, "https://images.unsplash.com/photo-1483729558449-99ef09a8c325"),
            createAttraction("Plage de Copacabana", "Plage urbaine emblématique de 4 km bordée par un boulevard pavé en mosaïque, populaire pour le bronzage, les sports et les événements.", "Copacabana", 0.0, 4, "https://images.unsplash.com/photo-1619546952812-520e98064a52")
        ));

        // Affectation des attractions aux destinations
        List<Attraction> allAttractions = new ArrayList<>();
        for (Destination destination : destinations) {
            List<Attraction> destinationAttractions = attractionsByDestination.get(destination.getName());
            if (destinationAttractions != null) {
                for (Attraction attraction : destinationAttractions) {
                    attraction.setDestination(destination);
                    allAttractions.add(attraction);
                }
            }
        }

        // Sauvegarde des attractions
        attractionRepository.saveAll(allAttractions);
        System.out.println(allAttractions.size() + " attractions ont été créées avec succès !");
    }

    private Attraction createAttraction(String name, String description, String location, Double price, Integer rating, String imageUrl) {
        Attraction attraction = new Attraction();
        attraction.setName(name);
        attraction.setDescription(description);
        attraction.setLocation(location);
        attraction.setPrice(price);
        attraction.setRating(rating);
        attraction.setImageUrl(imageUrl);
        return attraction;
    }
}
