package com.renault.garage.controller;

import com.renault.garage.dto.VehicleDTO;
import com.renault.garage.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des véhicules.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Ajoute un véhicule à un garage.
     * POST /api/garages/{garageId}/vehicles
     */
    @PostMapping("/garages/{garageId}/vehicles")
    public ResponseEntity<VehicleDTO> addVehicleToGarage(
            @PathVariable Long garageId,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO createdVehicle = vehicleService.addVehicleToGarage(garageId, vehicleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicle);
    }

    /**
     * Récupère un véhicule par son ID.
     * GET /api/vehicles/{id}
     */
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    /**
     * Récupère tous les véhicules d'un garage.
     * GET /api/garages/{garageId}/vehicles
     */
    @GetMapping("/garages/{garageId}/vehicles")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByGarage(@PathVariable Long garageId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByGarageId(garageId);
        return ResponseEntity.ok(vehicles);
    }

    /**
     * Récupère tous les véhicules d'un modèle donné.
     * GET /api/vehicles?model={model}
     */
    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByModel(
            @RequestParam(required = false) String model) {
        if (model != null && !model.isEmpty()) {
            List<VehicleDTO> vehicles = vehicleService.getVehiclesByModel(model);
            return ResponseEntity.ok(vehicles);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Met à jour un véhicule.
     * PUT /api/vehicles/{id}
     */
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
        return ResponseEntity.ok(updatedVehicle);
    }

    /**
     * Supprime un véhicule.
     * DELETE /api/vehicles/{id}
     */
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
