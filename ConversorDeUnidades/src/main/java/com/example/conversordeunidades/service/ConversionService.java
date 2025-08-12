package com.example.conversordeunidades.service;

import com.example.conversordeunidades.model.Unit;

/**
 * Servicio que maneja todas las conversiones de unidades
 * Implementa la lógica de conversión para diferentes tipos de unidades
 */
public class ConversionService {

    /**
     * Convierte un valor de una unidad a otra
     * @param value Valor a convertir
     * @param fromUnit Unidad de origen
     * @param toUnit Unidad de destino
     * @return Valor convertido
     * @throws IllegalArgumentException Si las unidades no son del mismo tipo
     */
    public static double convert(double value, Unit fromUnit, Unit toUnit) {
        // Validar que las unidades sean del mismo tipo
        if (fromUnit.getType() != toUnit.getType()) {
            throw new IllegalArgumentException(
                    "No se puede convertir entre diferentes tipos de unidades: "
                            + fromUnit.getType() + " y " + toUnit.getType()
            );
        }

        // Si son la misma unidad, retornar el mismo valor
        if (fromUnit == toUnit) {
            return value;
        }

        // Realizar la conversión según el tipo
        return switch (fromUnit.getType()) {
            case LONGITUD -> convertLength(value, fromUnit, toUnit);
            case PESO -> convertWeight(value, fromUnit, toUnit);
            case TEMPERATURA -> convertTemperature(value, fromUnit, toUnit);
        };
    }

    /**
     * Convierte unidades de longitud
     */
    private static double convertLength(double value, Unit from, Unit to) {
        // Convertir a metros (unidad base) y luego a la unidad destino
        double valueInMeters = value * from.getConversionFactor();
        return valueInMeters / to.getConversionFactor();
    }

    /**
     * Convierte unidades de peso
     */
    private static double convertWeight(double value, Unit from, Unit to) {
        // Convertir a kilogramos (unidad base) y luego a la unidad destino
        double valueInKg = value * from.getConversionFactor();
        return valueInKg / to.getConversionFactor();
    }

    /**
     * Convierte unidades de temperatura
     */
    private static double convertTemperature(double value, Unit from, Unit to) {
        // Primero convertir a Celsius como base
        double celsius = switch (from) {
            case CELSIUS -> value;
            case FAHRENHEIT -> (value - 32) * 5.0 / 9.0;
            case KELVIN -> value - 273.15;
            default -> throw new IllegalArgumentException("Unidad de temperatura no válida: " + from);
        };

        // Luego convertir de Celsius a la unidad destino
        return switch (to) {
            case CELSIUS -> celsius;
            case FAHRENHEIT -> (celsius * 9.0 / 5.0) + 32;
            case KELVIN -> celsius + 273.15;
            default -> throw new IllegalArgumentException("Unidad de temperatura no válida: " + to);
        };
    }

    /**
     * Valida si un string es un número válido
     */
    public static boolean isValidNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parsea un string a double de forma segura
     */
    public static double parseDouble(String input) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            throw new NumberFormatException("El valor no puede estar vacío");
        }

        return Double.parseDouble(input.trim());
    }
}