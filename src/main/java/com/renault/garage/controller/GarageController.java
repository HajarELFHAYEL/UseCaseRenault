package com.renault.garage.controller;

import com.renault.garage.dto.GarageDTO;
import com.renault.garage.service.GarageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des garages.
 */
@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    /**
     * Crée un nouveau garage.
     * POST /api/garages
     */
    @PostMapping
    public ResponseEntity<GarageDTO> createGarage(@Valid @RequestBody GarageDTO garageDTO) {
        GarageDTO createdGarage = garageService.createGarage(garageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGarage);
    }

    /**
     * Récupère un garage par son ID.
     * GET /api/garages/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<GarageDTO> getGarageById(@PathVariable Long id) {
        GarageDTO garage = garageService.getGarageById(id);
        return ResponseEntity.ok(garage);
    }

    /**
     * Récupère tous les garages avec pagination et tri.
     * GET /api/garages?page=0&size=10&sort=name,asc
     */
    @GetMapping
    public ResponseEntity<Page<GarageDTO>> getAllGarages(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<GarageDTO> garages = garageService.getAllGarages(pageable);
        return ResponseEntity.ok(garages);
    }

    /**
     * Met à jour un garage.
     * PUT /api/garages/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<GarageDTO> updateGarage(
            @PathVariable Long id,
            @Valid @RequestBody GarageDTO garageDTO) {
        GarageDTO updatedGarage = garageService.updateGarage(id, garageDTO);
        return ResponseEntity.ok(updatedGarage);
    }

    /**
     * Supprime un garage.
     * DELETE /api/garages/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }
}
