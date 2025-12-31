# Renault Garage Management Microservice

Microservice Spring Boot pour la gestion des garages, véhicules et accessoires du réseau Renault.

## Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Technologies](#-technologies)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [API Endpoints](#-api-endpoints)
- [Contraintes Métiers](#-contraintes-métiers)
- [Tests](#-tests)
- [Architecture](#-architecture)

## Fonctionnalités

### Gestion des Garages
- CRUD complet (création, lecture, modification, suppression)
- Liste paginée avec tri (par nom, ville, etc.)
- Horaires d'ouverture par jour de la semaine

### Gestion des Véhicules
- Ajout/modification/suppression de véhicules dans un garage
- Recherche par modèle dans tous les garages
- Quota maximum de 50 véhicules par garage

### Gestion des Accessoires
- Ajout/modification/suppression d'accessoires sur un véhicule
- Gestion du prix et du type d'accessoire

### Recherche Avancée
- Recherche de garages par type de carburant
- Recherche de garages par disponibilité d'un accessoire

### Event-Driven (Kafka)
- Publication automatique d'événements à la création de véhicules
- Consumer pour traitement des événements

## Technologies

| Technologie | Version |
|-------------|---------|
| Java | 17+ |
| Spring Boot | 3.2.1 |
| Spring Data JPA | 3.2.x |
| Spring Kafka | 3.1.x |
| H2 Database | Runtime |
| Lombok | Latest |
| JUnit 5 | 5.10.x |

## Installation

### Prérequis

- Java 17 ou supérieur
- Maven 3.8+
- (Optionnel) Apache Kafka pour les événements

### Cloner et compiler

```bash

# Compiler le projet
mvn clean compile

# Lancer les tests
mvn test

# Démarrer l'application
mvn spring-boot:run
```

## Utilisation

### Démarrer l'application

```bash
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`

### Console H2

Accessible à `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:garagedb`
- Username: `sa`
- Password: *(vide)*

## API Endpoints

### Garages

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/garages` | Créer un garage |
| `GET` | `/api/garages/{id}` | Récupérer un garage |
| `GET` | `/api/garages?page=0&size=10&sort=name,asc` | Liste paginée |
| `PUT` | `/api/garages/{id}` | Modifier un garage |
| `DELETE` | `/api/garages/{id}` | Supprimer un garage |

### Véhicules

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/garages/{garageId}/vehicles` | Ajouter un véhicule |
| `GET` | `/api/garages/{garageId}/vehicles` | Véhicules d'un garage |
| `GET` | `/api/vehicles?model={model}` | Recherche par modèle |
| `PUT` | `/api/vehicles/{id}` | Modifier un véhicule |
| `DELETE` | `/api/vehicles/{id}` | Supprimer un véhicule |

### Accessoires

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/vehicles/{vehicleId}/accessories` | Ajouter un accessoire |
| `GET` | `/api/vehicles/{vehicleId}/accessories` | Accessoires d'un véhicule |
| `PUT` | `/api/accessories/{id}` | Modifier un accessoire |
| `DELETE` | `/api/accessories/{id}` | Supprimer un accessoire |

### Recherche

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/search/garages?fuelType=ELECTRIQUE` | Garages par type carburant |
| `GET` | `/api/search/garages?accessory=GPS` | Garages avec accessoire |

## Exemples d'utilisation

### Créer un garage

```bash
curl -X POST http://localhost:8080/api/garages \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Garage Paris",
    "address": "123 Rue de Paris",
    "telephone": "0123456789",
    "email": "paris@renault.fr"
  }'
```

### Ajouter un véhicule

```bash
curl -X POST http://localhost:8080/api/garages/1/vehicles \
  -H "Content-Type: application/json" \
  -d '{
    "brand": "Renault",
    "model": "Clio",
    "anneeFabrication": 2024,
    "typeCarburant": "ELECTRIQUE"
  }'
```

### Ajouter un accessoire

```bash
curl -X POST http://localhost:8080/api/vehicles/1/accessories \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "GPS Navigation",
    "description": "Système GPS intégré",
    "prix": 499.99,
    "type": "MULTIMEDIA"
  }'
```

## Contraintes Métiers

| Contrainte | Description |
|------------|-------------|
| **Quota Véhicules** | Maximum 50 véhicules par garage |
| **Champs Obligatoires Garage** | name, address, telephone, email |
| **Champs Obligatoires Véhicule** | brand, model, anneeFabrication, typeCarburant |
| **Champs Obligatoires Accessoire** | nom, prix, type |

### Types de Carburant
- `ESSENCE`
- `DIESEL`
- `ELECTRIQUE`
- `HYBRIDE`
- `HYBRIDE_RECHARGEABLE`
- `GPL`

### Types d'Accessoires
- `INTERIEUR`
- `EXTERIEUR`
- `SECURITE`
- `CONFORT`
- `MULTIMEDIA`
- `PERFORMANCE`

## Tests

Le projet inclut des tests unitaires et d'intégration complets.

```bash
# Lancer tous les tests
mvn test

# Lancer un test spécifique
mvn test -Dtest=VehicleServiceTest
```

### Résultats des Tests

| Classe de Test | Tests | Statut |
|----------------|-------|--------|
| GarageServiceTest | 7 | ✅ |
| VehicleServiceTest | 8 | ✅ |
| AccessoryServiceTest | 7 | ✅ |
| GarageControllerIntegrationTest | 7 | ✅ |
| VehicleControllerIntegrationTest | 7 | ✅ |
| **Total** | **36** | ✅ |

## Architecture

```
src/main/java/com/renault/garage/
├── GarageManagementApplication.java
├── config/
│   └── KafkaConfig.java
├── controller/
│   ├── GarageController.java
│   ├── VehicleController.java
│   ├── AccessoryController.java
│   └── SearchController.java
├── dto/
│   ├── GarageDTO.java
│   ├── VehicleDTO.java
│   ├── AccessoryDTO.java
│   └── OpeningHoursDTO.java
├── entity/
│   ├── Garage.java
│   ├── Vehicle.java
│   ├── Accessory.java
│   └── GarageOpeningHours.java
├── enums/
│   ├── FuelType.java
│   └── AccessoryType.java
├── event/
│   └── VehicleCreatedEvent.java
├── exception/
│   ├── GarageNotFoundException.java
│   ├── VehicleNotFoundException.java
│   ├── AccessoryNotFoundException.java
│   ├── GarageCapacityExceededException.java
│   └── GlobalExceptionHandler.java
├── kafka/
│   ├── VehiclePublisher.java
│   └── VehicleEventConsumer.java
├── mapper/
│   ├── GarageMapper.java
│   ├── VehicleMapper.java
│   └── AccessoryMapper.java
├── repository/
│   ├── GarageRepository.java
│   ├── VehicleRepository.java
│   └── AccessoryRepository.java
└── service/
    ├── GarageService.java
    ├── VehicleService.java
    └── AccessoryService.java
```