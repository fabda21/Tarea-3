package com.example.conversordeunidades.service;

import com.example.conversordeunidades.model.Unit;


public class ConversionService {


    public static double convert(double value, Unit fromUnit, Unit toUnit) {

        if (fromUnit.getType() != toUnit.getType()) {
            throw new IllegalArgumentException(
                    "No se puede convertir entre diferentes tipos de unidades: "
                            + fromUnit.getType() + " y " + toUnit.getType()
            );
        }


        if (fromUnit == toUnit) {
            return value;
        }


        return switch (fromUnit.getType()) {
            case LONGITUD -> convertLength(value, fromUnit, toUnit);
            case PESO -> convertWeight(value, fromUnit, toUnit);
            case TEMPERATURA -> convertTemperature(value, fromUnit, toUnit);
        };
    }


    private static double convertLength(double value, Unit from, Unit to) {

        double valueInMeters = value * from.getConversionFactor();
        return valueInMeters / to.getConversionFactor();
    }


    private static double convertWeight(double value, Unit from, Unit to) {

        double valueInKg = value * from.getConversionFactor();
        return valueInKg / to.getConversionFactor();
    }


    private static double convertTemperature(double value, Unit from, Unit to) {

        double celsius = switch (from) {
            case CELSIUS -> value;
            case FAHRENHEIT -> (value - 32) * 5.0 / 9.0;
            case KELVIN -> value - 273.15;
            default -> throw new IllegalArgumentException("Unidad de temperatura no válida: " + from);
        };


        return switch (to) {
            case CELSIUS -> celsius;
            case FAHRENHEIT -> (celsius * 9.0 / 5.0) + 32;
            case KELVIN -> celsius + 273.15;
            default -> throw new IllegalArgumentException("Unidad de temperatura no válida: " + to);
        };
    }


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


    public static double parseDouble(String input) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            throw new NumberFormatException("El valor no puede estar vacío");
        }

        return Double.parseDouble(input.trim());
    }
}