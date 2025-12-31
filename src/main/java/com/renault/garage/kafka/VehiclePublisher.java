package com.renault.garage.kafka;

import com.renault.garage.entity.Vehicle;
import com.renault.garage.event.VehicleCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publisher Kafka pour les événements de véhicules.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VehiclePublisher {

    private final KafkaTemplate<String, VehicleCreatedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.vehicle-created:vehicle-created}")
    private String vehicleCreatedTopic;

    /**
     * Publie un événement lors de la création d'un véhicule.
     */
    public void publishVehicleCreated(Vehicle vehicle) {
        try {
            VehicleCreatedEvent event = VehicleCreatedEvent.of(
                    vehicle.getId(),
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    vehicle.getAnneeFabrication(),
                    vehicle.getTypeCarburant().name(),
                    vehicle.getGarage() != null ? vehicle.getGarage().getId() : null,
                    vehicle.getGarage() != null ? vehicle.getGarage().getName() : null
            );

            kafkaTemplate.send(vehicleCreatedTopic, String.valueOf(vehicle.getId()), event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Événement véhicule créé publié avec succès: vehicleId={}, topic={}, partition={}", 
                                    vehicle.getId(), 
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().partition());
                        } else {
                            log.error("Erreur lors de la publication de l'événement véhicule créé: vehicleId={}", 
                                    vehicle.getId(), ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'événement véhicule: {}", e.getMessage(), e);
        }
    }
}
