package com.converter.model;


public enum Unit {
    // Unidades de Longitud (factor de conversión a metros)
    METROS("Metros", ConversionType.LONGITUD, "m", 1.0),
    CENTIMETROS("Centímetros", ConversionType.LONGITUD, "cm", 0.01),
    PULGADAS("Pulgadas", ConversionType.LONGITUD, "in", 0.0254),
    PIES("Pies", ConversionType.LONGITUD, "ft", 0.3048),
    YARDAS("Yardas", ConversionType.LONGITUD, "yd", 0.9144),

    // Unidades de Peso (factor de conversión a kilogramos)
    KILOGRAMOS("Kilogramos", ConversionType.PESO, "kg", 1.0),
    GRAMOS("Gramos", ConversionType.PESO, "g", 0.001),
    LIBRAS("Libras", ConversionType.PESO, "lb", 0.453592),
    ONZAS("Onzas", ConversionType.PESO, "oz", 0.0283495),

    // Unidades de Temperatura (manejo especial)
    CELSIUS("Celsius", ConversionType.TEMPERATURA, "°C", 1.0),
    FAHRENHEIT("Fahrenheit", ConversionType.TEMPERATURA, "°F", 1.0),
    KELVIN("Kelvin", ConversionType.TEMPERATURA, "K", 1.0);

    private final String displayName;
    private final ConversionType type;
    private final String symbol;
    private final double conversionFactor;

    Unit(String displayName, ConversionType type, String symbol, double conversionFactor) {
        this.displayName = displayName;
        this.type = type;
        this.symbol = symbol;
        this.conversionFactor = conversionFactor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ConversionType getType() {
        return type;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public String toString() {
        return displayName + " (" + symbol + ")";
    }

    /**
     * Obtiene todas las unidades de un tipo específico
     */
    public static Unit[] getUnitsOfType(ConversionType type) {
        return java.util.Arrays.stream(Unit.values())
                .filter(unit -> unit.getType() == type)
                .toArray(Unit[]::new);
    }
}