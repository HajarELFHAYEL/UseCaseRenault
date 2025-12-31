package com.renault.garage.enums;

/**
 * Types de carburant disponibles pour les véhicules.
 */
public enum FuelType {
    ESSENCE("Essence"),
    DIESEL("Diesel"),
    ELECTRIQUE("Électrique"),
    HYBRIDE("Hybride"),
    HYBRIDE_RECHARGEABLE("Hybride Rechargeable"),
    GPL("GPL");

    private final String displayName;

    FuelType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
