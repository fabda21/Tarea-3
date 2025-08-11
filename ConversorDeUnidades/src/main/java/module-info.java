module unitconverter {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.converter to javafx.fxml;
    opens com.converter.controller to javafx.fxml;
    opens com.example.conversordeunidades to javafx.fxml;

    exports com.converter;
    exports com.converter.controller;
    exports com.converter.model;
    exports com.converter.service;






}
