package com.renault.garage.kafka;

import com.renault.garage.event.VehicleCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer Kafka pour les événements de véhicules.
 */
@Component
@Slf4j
public class VehicleEventConsumer {

    /**
     * Consomme les événements de création de véhicules.
     * Dans un cas réel, on pourrait envoyer des notifications, mettre à jour des caches, etc.
     */
    @KafkaListener(
            topics = "${app.kafka.topics.vehicle-created:vehicle-created}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleVehicleCreated(VehicleCreatedEvent event) {
        log.info("=== Événement véhicule créé reçu ===");
        log.info("Vehicle ID: {}", event.vehicleId());
        log.info("Marque: {}", event.brand());
        log.info("Modèle: {}", event.model());
        log.info("Année: {}", event.anneeFabrication());
        log.info("Type carburant: {}", event.typeCarburant());
        log.info("Garage ID: {}", event.garageId());
        log.info("Garage: {}", event.garageName());
        log.info("Créé à: {}", event.createdAt());
        log.info("=====================================");
        
        // Ici on pourrait ajouter de la logique métier:
        // - Envoyer une notification par email
        // - Mettre à jour un cache
        // - Synchroniser avec un système externe
        // - Générer des statistiques
    }
}
