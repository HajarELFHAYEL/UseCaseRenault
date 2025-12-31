package com.renault.garage.controller;

import com.renault.garage.dto.AccessoryDTO;
import com.renault.garage.service.AccessoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des accessoires.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccessoryController {

    private final AccessoryService accessoryService;

    /**
     * Ajoute un accessoire à un véhicule.
     * POST /api/vehicles/{vehicleId}/accessories
     */
    @PostMapping("/vehicles/{vehicleId}/accessories")
    public ResponseEntity<AccessoryDTO> addAccessoryToVehicle(
            @PathVariable Long vehicleId,
            @Valid @RequestBody AccessoryDTO accessoryDTO) {
        AccessoryDTO createdAccessory = accessoryService.addAccessoryToVehicle(vehicleId, accessoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccessory);
    }

    /**
     * Récupère un accessoire par son ID.
     * GET /api/accessories/{id}
     */
    @GetMapping("/accessories/{id}")
    public ResponseEntity<AccessoryDTO> getAccessoryById(@PathVariable Long id) {
        AccessoryDTO accessory = accessoryService.getAccessoryById(id);
        return ResponseEntity.ok(accessory);
    }

    /**
     * Récupère tous les accessoires d'un véhicule.
     * GET /api/vehicles/{vehicleId}/accessories
     */
    @GetMapping("/vehicles/{vehicleId}/accessories")
    public ResponseEntity<List<AccessoryDTO>> getAccessoriesByVehicle(@PathVariable Long vehicleId) {
        List<AccessoryDTO> accessories = accessoryService.getAccessoriesByVehicleId(vehicleId);
        return ResponseEntity.ok(accessories);
    }

    /**
     * Met à jour un accessoire.
     * PUT /api/accessories/{id}
     */
    @PutMapping("/accessories/{id}")
    public ResponseEntity<AccessoryDTO> updateAccessory(
            @PathVariable Long id,
            @Valid @RequestBody AccessoryDTO accessoryDTO) {
        AccessoryDTO updatedAccessory = accessoryService.updateAccessory(id, accessoryDTO);
        return ResponseEntity.ok(updatedAccessory);
    }

    /**
     * Supprime un accessoire.
     * DELETE /api/accessories/{id}
     */
    @DeleteMapping("/accessories/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.noContent().build();
    }
}
