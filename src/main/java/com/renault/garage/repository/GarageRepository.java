package com.renault.garage.repository;

import com.renault.garage.entity.Garage;
import com.renault.garage.enums.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour les opérations sur les garages.
 */
@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {

    /**
     * Recherche les garages par nom (contient, insensible à la casse).
     */
    Page<Garage> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Recherche les garages qui contiennent des véhicules d'un type de carburant donné.
     */
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v WHERE v.typeCarburant = :fuelType")
    List<Garage> findByVehicleFuelType(@Param("fuelType") FuelType fuelType);

    /**
     * Recherche les garages qui contiennent au moins un véhicule avec un accessoire donné.
     */
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v JOIN v.accessories a WHERE LOWER(a.nom) LIKE LOWER(CONCAT('%', :accessoryName, '%'))")
    List<Garage> findByAccessoryName(@Param("accessoryName") String accessoryName);
}
