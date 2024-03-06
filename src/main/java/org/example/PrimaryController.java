package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import org.example.db.DBHelper;
import org.example.modelo.Mesa;
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
    @FXML
    private TextField numMesaTxtField, calcTxtField;
    private final DBHelper dbHelper = new DBHelper();
    private ObservableList<Producto> productos;
    private Mesa mesa;

    public void initialize(){
        productos = FXCollections.observableArrayList();
        cargarCategorias();
        generarTabla();
    }

    private void setBkgImg(String imgPath, Button btn){
        BackgroundFill myBF = new BackgroundFill(new ImagePattern(new Image(imgPath)), new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));

        btn.setBackground(new Background(myBF));
    }

    private void cargarCategorias(){
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
    private void selecCategoria(String catName){
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

    private void generarTabla(){
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
        String number = ((Button)e.getSource()).getText();
        if(number.equals("C")){
            calcTxtField.setText("");
        }else{
            calcTxtField.appendText(number);
        }
    }

    @FXML
    public void selecProducto(Event e){
        String nombreSelec = ((Label)((Button)e.getSource()).getParent().getChildrenUnmodifiable().get(1)).getText().toLowerCase();
        boolean enc = false;
        if(!productos.isEmpty()){
            for (Producto p: productos) {
                if(p.getNombre().equals(nombreSelec)){
                    p.aumentarUds();
                    enc = true;
                    break;
                }
            }
        }

        if(!enc){
            productos.add(dbHelper.getProducto(nombreSelec));
        }

        tablaPedido.getItems().setAll(productos);
    }

    @FXML
    public void selecMesa(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
            Parent root = loader.load();
            SecondaryController secondaryController = loader.getController();
            secondaryController.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("Selecciona una mesa");
            stage.setMaximized(true);
            stage.setScene(new Scene(root));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setMesa(Mesa mesa){

    }

    public ObservableList<Producto> getProductos(){
        return productos;
    }
}
