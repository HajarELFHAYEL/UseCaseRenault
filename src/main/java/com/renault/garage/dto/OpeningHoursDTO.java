package com.renault.garage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO pour les horaires d'ouverture d'un garage.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHoursDTO {

    private Long id;

    @NotNull(message = "Le jour de la semaine est obligatoire")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "L'heure d'ouverture est obligatoire")
    private LocalTime startTime;

    @NotNull(message = "L'heure de fermeture est obligatoire")
    private LocalTime endTime;
}
