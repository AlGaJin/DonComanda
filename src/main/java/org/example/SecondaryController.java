package org.example;

import java.util.LinkedList;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import org.example.db.DBHelper;
import org.example.modelo.Mesa;
import org.example.modelo.Producto;

/**
 * Controlador de la vista de selección de mesa
 */
public class SecondaryController {
    @FXML
    private GridPane mesasGridPane; //Grid que contiene los botones de las diferentes mesas
    private PrimaryController parentController; //El controlador que lanza esta nueva ventana
    private LinkedList<Mesa> mesas; //Lista de mesas que hay en el programa
    private final DBHelper dbHelper = new DBHelper(); //Conexión con la base de datos

    /**
     * Permite indicar quién es el controlador que lanza esta ventana
     * @param parentController Controlador que lanza la ventana
     */
    public  void setParentController(PrimaryController parentController){
        this.parentController = parentController;
    }

    /**
     * Se llama al inicializar el controlador y carga datos necesarios para el funcionamiento del programa
     */
    public void initialize(){
        mesas = dbHelper.getMesas();
        cargarMesas();
    }

    /**
     * Permite aplicar una imagen reescalable a un botón
     * @param imgPath La ruta a la imagen
     * @param btn El botón al que se le va a aplicar la imagen
     */
    private void setBkgImg(String imgPath, Button btn){
        BackgroundFill myBF = new BackgroundFill(new ImagePattern(new Image(imgPath)), new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));

        btn.setBackground(new Background(myBF));
    }

    /**
     * Prepara los diferentes botones para las mesas que hay, cargando imagen y estilo para indicar si está ocupada
     */
    private void cargarMesas(){
        ObservableList<Node> vboxes = mesasGridPane.getChildren();
        for (int i = 0; i < vboxes.size(); i++) {
            VBox vbox = (VBox) vboxes.get(i);
            Button mesaBtn = (Button) vbox.getChildren().get(0);
            Label ocupadaLabel = (Label) vbox.getChildren().get(1);
            Mesa mesa = mesas.get(i);

            mesaBtn.setText(String.valueOf(mesa.getNum()));
            setBkgImg("file:src/main/resources/Imagenes/mesa.png", mesaBtn);
            if(mesa.isOcupada()){
                ocupadaLabel.setText("Ocupada");
                vbox.setStyle("-fx-background-color: coral;");
            }

        }
    }

    /**
     * Acción para los botones de las diferentes mesas, permitiendo seleccionar una mesa e indicarselo al controllador
     * padre.
     * Si la mesa seleccionada está libre, el controlador padre no tenía una mesa seleccionada y tenía una cuenta
     * comenzada, esta cuenta se aplicará a la mesa seleccionada. De tal forma que la cuenta generará una factura en
     * base de dato con los productos registrados en la cuenta.
     * @param e Permite conocer el botón que acciona la función
     */
    @FXML
    private void selecMesa(Event e){
        String numMesa = ((Button)e.getSource()).getText();
        ObservableList<Producto> productos = parentController.getProductos();
        for (Mesa m: mesas) {
            if(m.getNum() == Integer.parseInt(numMesa)){
                if(!m.isOcupada()){
                    m.setIdFactura(dbHelper.generarFactura(m));
                    if(!productos.isEmpty() && !parentController.isMesaSelected()){
                        m.setProductos(productos);
                        dbHelper.crearFacturaDetalle(productos, m.getIdFactura());
                    }
                }
                dbHelper.updateMesaOcupada(m.getNum(), true);
                ((Stage) mesasGridPane.getScene().getWindow()).close(); //Se obtiene la escena y se cierra
                parentController.setMesa(m); //Se aplica la mesa seleccionada al controlador padre
            }
        }
    }
}