package com.renault.garage.repository;

import com.renault.garage.entity.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour les opérations sur les accessoires.
 */
@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    /**
     * Trouve tous les accessoires d'un véhicule.
     */
    List<Accessory> findByVehicleId(Long vehicleId);
}
