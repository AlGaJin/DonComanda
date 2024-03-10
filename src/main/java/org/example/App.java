package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal que permite lanzar y mostrar la aplicación
 */
public class App extends Application {

    private static Scene scene;

    /**
     * Implementación de la interfaz Application, permite indicar qué escena se va a iniar
     * @param stage La ventana en la que se va a mostrar la escena
     * @throws IOException Si no se encuentra el archivo fxml que contiene los datos de la vista de la ventana
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Carga un fxml
     * @param fxml Ruta de acceso al .fxml
     * @return El fxml cargado
     * @throws IOException Si no se encuentra el archivo especificado en la ruta proporcinada
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}