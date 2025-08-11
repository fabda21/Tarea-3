module com.example.conversordeunidades {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.conversordeunidades to javafx.fxml;
    exports com.example.conversordeunidades;
}