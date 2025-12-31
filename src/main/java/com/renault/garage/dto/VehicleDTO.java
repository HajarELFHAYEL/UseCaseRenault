package com.renault.garage.dto;

import com.renault.garage.enums.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création et modification d'un véhicule.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {

    private Long id;

    @NotBlank(message = "La marque est obligatoire")
    private String brand;

    @NotBlank(message = "Le modèle est obligatoire")
    private String model;

    @NotNull(message = "L'année de fabrication est obligatoire")
    private Integer anneeFabrication;

    @NotNull(message = "Le type de carburant est obligatoire")
    private FuelType typeCarburant;

    private Long garageId;
    
    private String garageName;
    
    private Integer accessoryCount;
}
