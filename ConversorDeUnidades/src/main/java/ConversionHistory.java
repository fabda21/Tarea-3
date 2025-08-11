package com.converter.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ConversionHistory {
    private final double inputValue;
    private final Unit fromUnit;
    private final Unit toUnit;
    private final double result;
    private final LocalDateTime timestamp;

    public ConversionHistory(double inputValue, Unit fromUnit, Unit toUnit, double result) {
        this.inputValue = inputValue;
        this.fromUnit = fromUnit;
        this.toUnit = toUnit;
        this.result = result;
        this.timestamp = LocalDateTime.now();
    }

    public double getInputValue() {
        return inputValue;
    }

    public Unit getFromUnit() {
        return fromUnit;
    }

    public Unit getToUnit() {
        return toUnit;
    }

    public double getResult() {
        return result;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("[%s] %.4f %s = %.4f %s",
                timestamp.format(formatter),
                inputValue,
                fromUnit.getSymbol(),
                result,
                toUnit.getSymbol()
        );
    }
}
