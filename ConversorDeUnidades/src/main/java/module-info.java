module com.example.conversordeunidades {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.conversordeunidades to javafx.fxml;
    exports com.example.conversordeunidades;
    exports com.converter.controller;
    opens com.converter.controller to javafx.fxml;
}