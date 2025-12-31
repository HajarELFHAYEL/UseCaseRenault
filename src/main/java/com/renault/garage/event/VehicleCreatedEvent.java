package com.renault.garage.event;

import java.time.Instant;

/**
 * Événement publié lors de la création d'un véhicule.
 */
public record VehicleCreatedEvent(
        Long vehicleId,
        String brand,
        String model,
        Integer anneeFabrication,
        String typeCarburant,
        Long garageId,
        String garageName,
        Instant createdAt
) {
    public static VehicleCreatedEvent of(Long vehicleId, String brand, String model, 
                                         Integer anneeFabrication, String typeCarburant,
                                         Long garageId, String garageName) {
        return new VehicleCreatedEvent(
                vehicleId, brand, model, anneeFabrication, typeCarburant,
                garageId, garageName, Instant.now()
        );
    }
}
