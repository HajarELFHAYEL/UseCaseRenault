package com.renault.garage.repository;

import com.renault.garage.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour les opérations sur les véhicules.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * Trouve tous les véhicules d'un garage.
     */
    List<Vehicle> findByGarageId(Long garageId);

    /**
     * Trouve tous les véhicules d'un modèle donné dans tous les garages.
     */
    List<Vehicle> findByModelIgnoreCase(String model);

    /**
     * Trouve tous les véhicules d'un modèle donné (recherche partielle).
     */
    @Query("SELECT v FROM Vehicle v WHERE LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%'))")
    List<Vehicle> findByModelContaining(@Param("model") String model);

    /**
     * Compte le nombre de véhicules dans un garage.
     */
    long countByGarageId(Long garageId);
}
