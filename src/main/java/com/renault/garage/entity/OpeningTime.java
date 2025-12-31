package com.renault.garage.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Représente une plage horaire d'ouverture.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpeningTime {
    
    private LocalTime startTime;
    private LocalTime endTime;
}
