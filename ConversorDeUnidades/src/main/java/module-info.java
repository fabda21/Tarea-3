module conversordeunidades {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.conversordeunidades to javafx.fxml;
    opens com.example.conversordeunidades.controller to javafx.fxml;

    exports com.example.conversordeunidades;
    exports com.example.conversordeunidades.controller;
    exports com.example.conversordeunidades.model;
    exports com.example.conversordeunidades.service;
}





