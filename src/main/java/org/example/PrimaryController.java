package org.example;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import org.example.modelo.Producto;

import java.io.File;
import java.util.LinkedList;

public class PrimaryController {

    @FXML
    private TableView<Producto> tablaPedido;
    @FXML
    private GridPane categoriasGridPane;
    @FXML
    private GridPane productosGridPane;

    public void initialize(){
        cargarCategorias();
        generarTabla();
    }

    private void setBkgImg(String imgPath, Button btn){
        BackgroundFill myBF = new BackgroundFill(new ImagePattern(new Image(imgPath)), new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));

        btn.setBackground(new Background(myBF));
    }

    public void cargarCategorias(){
        ObservableList<Node> children = categoriasGridPane.getChildren();
        for (Node vbox :children) {
            ObservableList<Node> vboxChildren = ((VBox)vbox).getChildren();
            String catName = ((Label)vboxChildren.get(1)).getText().toLowerCase();
            Button catBtn = ((Button) vboxChildren.get(0));

            String imgPath = "file:src/main/resources/Images/"+catName + ".png";

            setBkgImg(imgPath, catBtn);

            catBtn.setOnAction(event -> {
                selecCategoria(catName);
            });
        }
    }

    @FXML
    public void selecCategoria(String catName){
        File dir = new File("src/main/resources/Images/"+catName);
        File imagenes[] = dir.listFiles();

        ObservableList<Node> childen = productosGridPane.getChildren();
        int i = 0;
        for(Node vbox: childen){
            ObservableList<Node> vboxChildren = ((VBox) vbox).getChildren();
            if(i < imagenes.length){
                vbox.setVisible(true);
                for(Node node: vboxChildren){
                    if(node instanceof Label){
                        ((Label)node).setText(imagenes[i].getName().split("\\.")[0]);
                    }else if(node instanceof Button){
                        setBkgImg("file:" + imagenes[i].getPath(), (Button) node);
                    }
                }
            }else{
                vbox.setVisible(false);
            }
            i++;
        }
    }

    public void generarTabla(){
        TableColumn<Producto, String> colProducto = new TableColumn<>("Producto");
        TableColumn<Producto, Integer> colUnidades = new TableColumn<>("Uds.");
        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Importe");
        TableColumn<Producto, Integer> colDto = new TableColumn<>("Dto %");
        TableColumn<Producto, Double> colTotal = new TableColumn<>("Total");

        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUnidades.setCellValueFactory(new PropertyValueFactory<>("uds"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDto.setCellValueFactory(new PropertyValueFactory<>("dto"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tablaPedido.setPlaceholder(new Label("No se ha pedido nada"));
        tablaPedido.getColumns().addAll(new TableColumn[]{colProducto, colUnidades, colPrecio, colDto, colTotal});
    }

    @FXML
    public void calculadora(Event e){

    }

    @FXML
    public void selecProducto(Event e){

    }
}
