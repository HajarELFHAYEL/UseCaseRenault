package com.renault.garage.dto;

import com.renault.garage.enums.AccessoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO pour la création et modification d'un accessoire.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessoryDTO {

    private Long id;

    @NotBlank(message = "Le nom de l'accessoire est obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal prix;

    @NotNull(message = "Le type d'accessoire est obligatoire")
    private AccessoryType type;

    private Long vehicleId;
}
