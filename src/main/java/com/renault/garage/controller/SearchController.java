package com.renault.garage.controller;

import com.renault.garage.dto.GarageDTO;
import com.renault.garage.enums.FuelType;
import com.renault.garage.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour les recherches avancées.
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final GarageService garageService;

    /**
     * Recherche les garages par type de carburant.
     * GET /api/search/garages?fuelType=ELECTRIQUE
     */
    @GetMapping("/garages")
    public ResponseEntity<List<GarageDTO>> searchGarages(
            @RequestParam(required = false) FuelType fuelType,
            @RequestParam(required = false) String accessory) {
        
        if (fuelType != null) {
            List<GarageDTO> garages = garageService.findByVehicleFuelType(fuelType);
            return ResponseEntity.ok(garages);
        }
        
        if (accessory != null && !accessory.isEmpty()) {
            List<GarageDTO> garages = garageService.findByAccessoryName(accessory);
            return ResponseEntity.ok(garages);
        }
        
        return ResponseEntity.badRequest().build();
    }
}
