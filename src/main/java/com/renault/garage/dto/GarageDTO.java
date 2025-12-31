package com.renault.garage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour la création et modification d'un garage.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageDTO {

    private Long id;

    @NotBlank(message = "Le nom du garage est obligatoire")
    private String name;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    private List<OpeningHoursDTO> horairesOuverture;
    
    private Integer vehicleCount;
}
