# Architecture de TravelApp

## Vue d'ensemble
TravelApp est une application web Spring Boot qui intègre un chatbot intelligent alimenté par l'API Groq Cloud. L'application est conteneurisée avec Docker et déployée via un pipeline CI/CD Jenkins.

## Composants principaux

### Backend (Spring Boot)
- **Controllers** : Gèrent les requêtes HTTP et les interactions utilisateur
  - GroqChatController : API REST pour le chatbot
  - DirectGroqController : Version simplifiée du contrôleur pour tests directs
- **Services** : Contiennent la logique métier
  - GroqApiService : Communique avec l'API Groq Cloud
- **Modèles** : Représentent les entités de données
  - Destination, User, etc.

### Intégration API
- **Groq Cloud API** : API externe utilisée pour les fonctionnalités de chatbot IA
  - Modèle : llama-4-scout-17b-16e-instruct
  - Communication via HTTP REST

### Frontend
- **Thymeleaf** : Moteur de templates pour le rendu côté serveur
- **Bootstrap** : Framework CSS pour l'interface utilisateur
- **JavaScript** : Interactivité côté client, notamment pour le chatbot

## Infrastructure DevOps
- **Contrôle de Version** : Git/GitHub
- **Build Automation** : Maven
- **Conteneurisation** : Docker
- **CI/CD** : Jenkins
- **Déploiement** : Local Docker (Dev), potentiellement AWS (Prod)

## Flux de données
1. L'utilisateur accède à l'application via un navigateur web
2. Les requêtes sont traitées par les contrôleurs Spring Boot
3. Pour les interactions avec le chatbot, l'API Groq est appelée
4. Les réponses sont formatées et renvoyées à l'utilisateur
5. En cas d'indisponibilité de l'API, un mécanisme de fallback est activé

## Sécurité
- Spring Security pour la gestion des authentifications
- Sécurisation des endpoints API
- Gestion sécurisée des clés API via les propriétés d'application