package com.example.conversordeunidades.model;

/**
 * Enum que representa los tipos de conversión disponibles
 */
public enum ConversionType {
    LONGITUD("Longitud"),
    PESO("Peso"),
    TEMPERATURA("Temperatura");

    private final String displayName;

    ConversionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}