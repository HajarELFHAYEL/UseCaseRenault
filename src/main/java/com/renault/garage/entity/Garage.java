package com.renault.garage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un garage du réseau Renault.
 */
@Entity
@Table(name = "garages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garage {

    public static final int MAX_VEHICLES = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du garage est obligatoire")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "L'adresse est obligatoire")
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Column(nullable = false)
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GarageOpeningHours> horairesOuverture = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();

    /**
     * Vérifie si le garage peut accueillir un nouveau véhicule.
     */
    public boolean canAddVehicle() {
        return vehicles.size() < MAX_VEHICLES;
    }

    /**
     * Retourne le nombre de véhicules actuellement dans le garage.
     */
    public int getVehicleCount() {
        return vehicles.size();
    }

    /**
     * Ajoute un véhicule au garage.
     */
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setGarage(this);
    }

    /**
     * Retire un véhicule du garage.
     */
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicle.setGarage(null);
    }

    /**
     * Ajoute des horaires d'ouverture au garage.
     */
    public void addOpeningHours(GarageOpeningHours openingHours) {
        horairesOuverture.add(openingHours);
        openingHours.setGarage(this);
    }
}
