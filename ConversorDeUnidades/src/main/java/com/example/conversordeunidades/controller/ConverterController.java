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


    private final List<ConversionHistory> conversionHistory = new ArrayList<>();
    private final int MAX_HISTORY = 10;


    private final DecimalFormat decimalFormat = new DecimalFormat("#.####");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComponents();
        setupEventHandlers();
        setupValidations();
    }


    private void setupComponents() {

        typeComboBox.setItems(FXCollections.observableArrayList(ConversionType.values()));


        resultField.setEditable(false);


        historyTextArea.setEditable(false);
        historyTextArea.setText("Historial de conversiones:\n");


        if (!typeComboBox.getItems().isEmpty()) {
            typeComboBox.getSelectionModel().select(0);
            updateUnitComboBoxes();
        }
    }


    private void setupEventHandlers() {

        typeComboBox.setOnAction(e -> updateUnitComboBoxes());


        convertButton.setOnAction(e -> performConversion());


        clearButton.setOnAction(e -> clearAll());


        inputField.setOnAction(e -> performConversion());
    }


    private void setupValidations() {

        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !ConversionService.isValidNumber(newValue)) {
                inputField.setText(oldValue);
            }
        });
    }


    private void updateUnitComboBoxes() {
        ConversionType selectedType = typeComboBox.getSelectionModel().getSelectedItem();
        if (selectedType != null) {
            Unit[] units = Unit.getUnitsOfType(selectedType);

            ObservableList<Unit> unitList = FXCollections.observableArrayList(units);
            fromUnitComboBox.setItems(unitList);
            toUnitComboBox.setItems(unitList);


            if (units.length >= 2) {
                fromUnitComboBox.getSelectionModel().select(0);
                toUnitComboBox.getSelectionModel().select(1);
            }
        }
    }


    @FXML
    private void performConversion() {
        try {

            if (!validateInput()) {
                return;
            }


            double inputValue = ConversionService.parseDouble(inputField.getText());
            Unit fromUnit = fromUnitComboBox.getSelectionModel().getSelectedItem();
            Unit toUnit = toUnitComboBox.getSelectionModel().getSelectedItem();


            if (fromUnit == toUnit) {
                showAlert(AlertType.WARNING, "Advertencia",
                        "Por favor selecciona unidades diferentes para la conversión.");
                return;
            }


            double result = ConversionService.convert(inputValue, fromUnit, toUnit);


            resultField.setText(decimalFormat.format(result));


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


    private boolean validateInput() {

        if (inputField.getText().trim().isEmpty()) {
            showAlert(AlertType.WARNING, "Campo Requerido",
                    "Por favor ingresa un valor para convertir.");
            inputField.requestFocus();
            return false;
        }


        if (fromUnitComboBox.getSelectionModel().getSelectedItem() == null) {
            showAlert(AlertType.WARNING, "Selección Requerida",
                    "Por favor selecciona la unidad de origen.");
            fromUnitComboBox.requestFocus();
            return false;
        }


        if (toUnitComboBox.getSelectionModel().getSelectedItem() == null) {
            showAlert(AlertType.WARNING, "Selección Requerida",
                    "Por favor selecciona la unidad de destino.");
            toUnitComboBox.requestFocus();
            return false;
        }

        return true;
    }


    private void addToHistory(ConversionHistory conversion) {
        conversionHistory.add(0, conversion); // Agregar al inicio

        if (conversionHistory.size() > MAX_HISTORY) {
            conversionHistory.remove(conversionHistory.size() - 1);
        }


        updateHistoryDisplay();
    }



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


        historyTextArea.setScrollTop(0);
    }


    @FXML
    private void clearAll() {
        inputField.clear();
        resultField.clear();


        if (!fromUnitComboBox.getItems().isEmpty()) {
            fromUnitComboBox.getSelectionModel().select(0);
        }
        if (toUnitComboBox.getItems().size() >= 2) {
            toUnitComboBox.getSelectionModel().select(1);
        }


        inputField.requestFocus();
    }


    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}