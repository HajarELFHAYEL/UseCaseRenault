package com.renault.garage.enums;

/**
 * Types d'accessoires disponibles pour les véhicules.
 */
public enum AccessoryType {
    INTERIEUR("Intérieur"),
    EXTERIEUR("Extérieur"),
    SECURITE("Sécurité"),
    CONFORT("Confort"),
    MULTIMEDIA("Multimédia"),
    PERFORMANCE("Performance");

    private final String displayName;

    AccessoryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
