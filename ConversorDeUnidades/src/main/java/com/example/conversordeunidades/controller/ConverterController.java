package com.example.conversordeunidades.controller;

import com.example.conversordeunidades.model.ConversionHistory;
import com.example.conversordeunidades.model.ConversionType;
import com.example.conversordeunidades.model.Unit;
import com.example.conversordeunidades.service.ConversionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador principal de la aplicación
 * Maneja toda la lógica de la interfaz de usuario
 */
public class ConverterController implements Initializable {

    // Elementos FXML
    @FXML private ComboBox<ConversionType> typeComboBox;
    @FXML private TextField inputField;
    @FXML private ComboBox<Unit> fromUnitComboBox;
    @FXML private ComboBox<Unit> toUnitComboBox;
    @FXML private TextField resultField;
    @FXML private TextArea historyTextArea;
    @FXML private Button convertButton;
    @FXML private Button clearButton;

    // Lista del historial de conversiones (máximo 10)
    private final List<ConversionHistory> conversionHistory = new ArrayList<>();
    private final int MAX_HISTORY = 10;

    // Formateador para mostrar números con 4 decimales
    private final DecimalFormat decimalFormat = new DecimalFormat("#.####");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComponents();
        setupEventHandlers();
        setupValidations();
    }

    /**
     * Configura los componentes iniciales
     */
    private void setupComponents() {
        // Configurar ComboBox de tipos de conversión
        typeComboBox.setItems(FXCollections.observableArrayList(ConversionType.values()));

        // Hacer el campo de resultado solo lectura
        resultField.setEditable(false);

        // Configurar área de historial
        historyTextArea.setEditable(false);
        historyTextArea.setText("Historial de conversiones:\n");

        // Seleccionar el primer tipo por defecto
        if (!typeComboBox.getItems().isEmpty()) {
            typeComboBox.getSelectionModel().select(0);
            updateUnitComboBoxes();
        }
    }

    /**
     * Configura los manejadores de eventos
     */
    private void setupEventHandlers() {
        // Cuando cambia el tipo de conversión, actualizar las unidades
        typeComboBox.setOnAction(e -> updateUnitComboBoxes());

        // Botón convertir
        convertButton.setOnAction(e -> performConversion());

        // Botón limpiar
        clearButton.setOnAction(e -> clearAll());

        // Enter en el campo de entrada también convierte
        inputField.setOnAction(e -> performConversion());
    }

    /**
     * Configura las validaciones de entrada
     */
    private void setupValidations() {
        // Validación para solo números en el campo de entrada
        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !ConversionService.isValidNumber(newValue)) {
                inputField.setText(oldValue);
            }
        });
    }

    /**
     * Actualiza los ComboBox de unidades basado en el tipo seleccionado
     */
    private void updateUnitComboBoxes() {
        ConversionType selectedType = typeComboBox.getSelectionModel().getSelectedItem();
        if (selectedType != null) {
            Unit[] units = Unit.getUnitsOfType(selectedType);

            ObservableList<Unit> unitList = FXCollections.observableArrayList(units);
            fromUnitComboBox.setItems(unitList);
            toUnitComboBox.setItems(unitList);

            // Seleccionar las primeras dos unidades por defecto
            if (units.length >= 2) {
                fromUnitComboBox.getSelectionModel().select(0);
                toUnitComboBox.getSelectionModel().select(1);
            }
        }
    }

    /**
     * Realiza la conversión principal
     */
    @FXML
    private void performConversion() {
        try {
            // Validar entrada
            if (!validateInput()) {
                return;
            }

            // Obtener valores
            double inputValue = ConversionService.parseDouble(inputField.getText());
            Unit fromUnit = fromUnitComboBox.getSelectionModel().getSelectedItem();
            Unit toUnit = toUnitComboBox.getSelectionModel().getSelectedItem();

            // Validar que no sean la misma unidad
            if (fromUnit == toUnit) {
                showAlert(AlertType.WARNING, "Advertencia",
                        "Por favor selecciona unidades diferentes para la conversión.");
                return;
            }

            // Realizar conversión
            double result = ConversionService.convert(inputValue, fromUnit, toUnit);

            // Mostrar resultado
            resultField.setText(decimalFormat.format(result));

            // Agregar al historial
            addToHistory(new ConversionHistory(inputValue, fromUnit, toUnit, result));

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error de Entrada",
                    "Por favor ingresa un número válido.");
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error de Conversión", e.getMessage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error Inesperado",
                    "Ha ocurrido un error: " + e.getMessage());
        }
    }

    /**
     * Valida la entrada del usuario
     */
    private boolean validateInput() {
        // Validar campo de entrada
        if (inputField.getText().trim().isEmpty()) {
            showAlert(AlertType.WARNING, "Campo Requerido",
                    "Por favor ingresa un valor para convertir.");
            inputField.requestFocus();
            return false;
        }

        // Validar selección de unidad origen
        if (fromUnitComboBox.getSelectionModel().getSelectedItem() == null) {
            showAlert(AlertType.WARNING, "Selección Requerida",
                    "Por favor selecciona la unidad de origen.");
            fromUnitComboBox.requestFocus();
            return false;
        }

        // Validar selección de unidad destino
        if (toUnitComboBox.getSelectionModel().getSelectedItem() == null) {
            showAlert(AlertType.WARNING, "Selección Requerida",
                    "Por favor selecciona la unidad de destino.");
            toUnitComboBox.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Agrega una conversión al historial
     */
    private void addToHistory(ConversionHistory conversion) {
        conversionHistory.add(0, conversion); // Agregar al inicio

        // Mantener solo las últimas 10 conversiones
        if (conversionHistory.size() > MAX_HISTORY) {
            conversionHistory.remove(conversionHistory.size() - 1);
        }

        // Actualizar el área de texto del historial
        updateHistoryDisplay();
    }

    /**
     * Actualiza la visualización del historial
     */
    private void updateHistoryDisplay() {
        StringBuilder historyText = new StringBuilder("Historial de conversiones:\n");
        historyText.append("─".repeat(50)).append("\n");

        if (conversionHistory.isEmpty()) {
            historyText.append("(Sin conversiones aún)\n");
        } else {
            for (ConversionHistory conversion : conversionHistory) {
                historyText.append(conversion.toString()).append("\n");
            }
        }

        historyTextArea.setText(historyText.toString());

        // Scroll automático hacia arriba para mostrar la conversión más reciente
        historyTextArea.setScrollTop(0);
    }

    /**
     * Limpia todos los campos
     */
    @FXML
    private void clearAll() {
        inputField.clear();
        resultField.clear();

        // Restablecer selecciones de ComboBox
        if (!fromUnitComboBox.getItems().isEmpty()) {
            fromUnitComboBox.getSelectionModel().select(0);
        }
        if (toUnitComboBox.getItems().size() >= 2) {
            toUnitComboBox.getSelectionModel().select(1);
        }

        // Enfocar el campo de entrada
        inputField.requestFocus();
    }

    /**
     * Muestra una alerta al usuario
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}