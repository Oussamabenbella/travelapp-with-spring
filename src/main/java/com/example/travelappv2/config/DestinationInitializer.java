package com.example.travelappv2.config;

import com.example.travelappv2.entity.Destination;
import com.example.travelappv2.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(2) // Exécuter après DataInitializer qui a l'ordre 1
public class DestinationInitializer implements CommandLineRunner {

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si des destinations existent déjà
        if (destinationRepository.count() > 0) {
            System.out.println("Des destinations existent déjà, initialisation ignorée.");
            return;
        }

        // Créer des destinations fictives
        List<Destination> destinations = Arrays.asList(
            createDestination(
                "Paris", 
                "France", 
                "La ville de l'amour avec la Tour Eiffel, le Louvre et Notre-Dame de Paris.", 
                "Paris est l'une des destinations touristiques les plus populaires au monde. Vous pourrez visiter des monuments emblématiques, déguster la cuisine française, vous promener le long de la Seine et profiter de l'ambiance unique de ses quartiers.",
                "https://images.unsplash.com/photo-1499856871958-5b9627545d1a",
                4.7
            ),
            createDestination(
                "Rome", 
                "Italie", 
                "La ville éternelle avec le Colisée, le Vatican et la fontaine de Trevi.", 
                "Rome est une ville où l'art, la culture et l'histoire se rencontrent à chaque coin de rue. Visitez le Colisée, le Forum romain, la Chapelle Sixtine et profitez de la délicieuse cuisine italienne.",
                "https://images.unsplash.com/photo-1531572753322-ad063cecc140",
                4.8
            ),
            createDestination(
                "Barcelone", 
                "Espagne", 
                "Ville méditerranéenne avec l'architecture de Gaudí et des plages magnifiques.", 
                "Barcelone offre un mélange parfait de plages, de culture et d'architecture. Explorez les œuvres de Gaudí comme la Sagrada Familia et le Parc Güell, flânez sur Las Ramblas et dégustez des tapas dans le quartier gothique.",
                "https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9",
                4.6
            ),
            createDestination(
                "Tokyo", 
                "Japon", 
                "Métropole ultra-moderne avec ses temples traditionnels et sa cuisine renommée.", 
                "Tokyo est une ville fascinante où tradition et modernité coexistent. Découvrez les temples anciens, les gratte-ciels futuristes, les jardins paisibles et la délicieuse cuisine japonaise.",
                "https://images.unsplash.com/photo-1533106497176-45ae19e68ba2",
                4.9
            ),
            createDestination(
                "New York", 
                "États-Unis", 
                "La ville qui ne dort jamais avec ses gratte-ciels et Central Park.", 
                "New York est une métropole dynamique offrant une expérience urbaine incomparable. Visitez Times Square, l'Empire State Building, Central Park, et profitez de sa scène artistique, culturelle et gastronomique diversifiée.",
                "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9",
                4.7
            ),
            createDestination(
                "Rio de Janeiro", 
                "Brésil", 
                "Ville côtière avec le Christ Rédempteur et de superbes plages.", 
                "Rio de Janeiro est connue pour ses plages emblématiques comme Copacabana et Ipanema, le pain de sucre, et le Christ Rédempteur. Profitez du carnaval, de la samba et des paysages à couper le souffle.",
                "https://images.unsplash.com/photo-1516306580123-e6e52b1b7b5f",
                4.5
            ),
            createDestination(
                "Marrakech", 
                "Maroc", 
                "Ville historique avec ses souks colorés et son architecture marocaine.", 
                "Marrakech vous séduira avec ses médinas animées, ses palais ornés et ses jardins luxuriants. Explorez les souks, visitez les palais et détendez-vous dans un hammam traditionnel.",
                "https://images.unsplash.com/photo-1597211684565-dca64d72bdfe",
                4.6
            ),
            createDestination(
                "Sydney", 
                "Australie", 
                "Ville portuaire avec son opéra emblématique et ses plages magnifiques.", 
                "Sydney offre un style de vie décontracté avec ses magnifiques plages, son port pittoresque et son architecture distinctive. Visitez l'Opéra de Sydney, grimpez sur le Harbour Bridge et détendez-vous à Bondi Beach.",
                "https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9",
                4.8
            ),
            createDestination(
                "Santorin", 
                "Grèce", 
                "Île volcanique avec ses maisons blanches à dômes bleus et ses couchers de soleil spectaculaires.", 
                "Santorin est connue pour ses vues panoramiques, ses villages perchés à flanc de falaise et ses plages volcaniques. Admirez les couchers de soleil à Oia, visitez les sites archéologiques et dégustez la cuisine grecque locale.",
                "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff",
                4.9
            ),
            createDestination(
                "Kyoto", 
                "Japon", 
                "Ancienne capitale impériale avec ses temples, jardins zen et maisons de geishas.", 
                "Kyoto représente l'essence de la culture japonaise traditionnelle avec ses temples séculaires, ses jardins zen et ses sanctuaires shintoïstes. Promenez-vous dans le quartier de Gion, participez à une cérémonie du thé et découvrez l'artisanat local.",
                "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e",
                4.8
            )
        );

        // Sauvegarder les destinations
        destinationRepository.saveAll(destinations);
        System.out.println("10 destinations ont été créées avec succès !");
    }

    private Destination createDestination(String name, String country, String shortDescription, String description, String imageUrl, double rating) {
        Destination destination = new Destination();
        destination.setName(name);
        destination.setCity(name); // Pour simplifier, on utilise le même nom pour la ville
        destination.setCountry(country);
        destination.setShortDescription(shortDescription);
        destination.setDescription(description);
        destination.setImageUrl(imageUrl);
        destination.setRating(rating);
        return destination;
    }
}
