package com.renault.garage.entity;

import com.renault.garage.enums.FuelType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un véhicule stocké dans un garage.
 */
@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La marque est obligatoire")
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "Le modèle est obligatoire")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "L'année de fabrication est obligatoire")
    @Column(name = "annee_fabrication", nullable = false)
    private Integer anneeFabrication;

    @NotNull(message = "Le type de carburant est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_carburant", nullable = false)
    private FuelType typeCarburant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Accessory> accessories = new ArrayList<>();

    /**
     * Ajoute un accessoire au véhicule.
     */
    public void addAccessory(Accessory accessory) {
        accessories.add(accessory);
        accessory.setVehicle(this);
    }

    /**
     * Retire un accessoire du véhicule.
     */
    public void removeAccessory(Accessory accessory) {
        accessories.remove(accessory);
        accessory.setVehicle(null);
    }
}
