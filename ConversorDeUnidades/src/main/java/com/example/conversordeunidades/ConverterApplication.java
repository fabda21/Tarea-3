package com.example.conversordeunidades;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class ConverterApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Buscar el archivo FXML en el classpath
        URL fxmlLocation = getClass().getResource("/com/example/conversordeunidades/converter-view.fxml");

        if (fxmlLocation == null) {
            // Intentar con la ubicación alternativa
            fxmlLocation = getClass().getResource("converter-view.fxml");
        }

        if (fxmlLocation == null) {
            throw new IOException("No se pudo encontrar el archivo converter-view.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);

        Scene scene = new Scene(fxmlLoader.load(), 500, 700);

        stage.setTitle("Conversor de Unidades");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}