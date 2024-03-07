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

public class SecondaryController {
    @FXML
    private GridPane mesasGridPane;
    private PrimaryController parentController;
    private LinkedList<Mesa> mesas;
    private final DBHelper dbHelper = new DBHelper();
    public  void setParentController(PrimaryController parentController){
        this.parentController = parentController;
    }

    public void initialize(){
        mesas = dbHelper.getMesas();
        cargarMesas();
    }

    private void setBkgImg(String imgPath, Button btn){
        BackgroundFill myBF = new BackgroundFill(new ImagePattern(new Image(imgPath)), new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));

        btn.setBackground(new Background(myBF));
    }

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
                    }
                }
                dbHelper.updateMesaOcupada(m.getNum(), true);
                ((Stage) mesasGridPane.getScene().getWindow()).close();
                parentController.setMesa(m);
            }
        }
    }
}