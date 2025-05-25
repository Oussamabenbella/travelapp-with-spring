# Étapes de déploiement de TravelApp

## Prérequis
- Java 17 ou supérieur
- Maven
- Docker Desktop
- Jenkins (optionnel pour CI/CD)

## Déploiement manuel

### 1. Build avec Maven
```bash
cd C:\Users\user\CascadeProjects\TravelAppV2
mvn clean package -DskipTests