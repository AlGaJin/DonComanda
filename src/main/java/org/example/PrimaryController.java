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

public class PrimaryController {

    @FXML
    private TableView<Producto> tablaPedido;
    @FXML
    private GridPane categoriasGridPane;
    @FXML
    private GridPane productosGridPane;
    @FXML
    private TextField numMesaTxtField, calcTxtField;
    @FXML
    private Label articulosLabel, udsLabel, totalLabel;
    private boolean mesaSelected;
    private final DBHelper dbHelper = new DBHelper();
    private ObservableList<Producto> productos;
    private Mesa mesa;

    public void initialize(){
        mesaSelected = false;
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

            String imgPath = "file:src/main/resources/Imagenes/Productos/"+catName + ".png";

            setBkgImg(imgPath, catBtn);

            catBtn.setOnAction(event -> {
                selecCategoria(catName);
            });
        }
    }

    @FXML
    private void selecCategoria(String catName){
        File dir = new File("src/main/resources/Imagenes/Productos/"+catName);
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

        if(mesa != null){
            dbHelper.addToDetalleFactura(dbHelper.getProducto(nombreSelec), mesa.getIdFactura());
        }
        updateTable();
    }

    private void generarTabla(){
        TableColumn<Producto, String> colProducto = new TableColumn<>("Producto");
        TableColumn<Producto, Integer> colUnidades = new TableColumn<>("Uds.");
        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Importe");
        TableColumn<Producto, Integer> colDto = new TableColumn<>("Dto %");
        TableColumn<Producto, Double> colTotal = new TableColumn<>("Total €");

        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUnidades.setCellValueFactory(new PropertyValueFactory<>("uds"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDto.setCellValueFactory(new PropertyValueFactory<>("dto"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tablaPedido.setPlaceholder(new Label("No se ha pedido nada"));
        tablaPedido.getColumns().addAll(new TableColumn[]{colProducto, colUnidades, colPrecio, colDto, colTotal});
    }

    public void updateTable(){
        if(mesa != null){
            mesa.setProductos(productos);

        }
        tablaPedido.getItems().setAll(productos);

        int articulos = productos.size();
        int uds = 0;
        double total = 0;
        for (Producto p: productos) {
            uds += p.getUds();
            total += p.getTotal();
        }

        articulosLabel.setText(String.valueOf(articulos));
        udsLabel.setText(String.valueOf(uds));
        totalLabel.setText(String.valueOf(total));
    }

    @FXML
    public void facturaSinMesaBtn(){
        mesa = null;
        numMesaTxtField.setText("");
        productos = FXCollections.observableArrayList();
        mesaSelected = false;
        updateTable();
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
    public void introBtn(){
        String uds = calcTxtField.getText();
        int idx = tablaPedido.getSelectionModel().getSelectedIndex();
        if(!uds.isEmpty() && idx != -1){
            if(Integer.parseInt(uds) == 0){
                eliminarBtn();
            }else {
                productos.get(idx).setUds(Integer.parseInt(uds));
            }
            calcTxtField.setText("");
            updateTable();
        }
    }

    @FXML
    public void eliminarBtn(){
        int idx = tablaPedido.getSelectionModel().getSelectedIndex();
        if(idx != -1){
            Producto p = productos.remove(idx);
            if(mesa != null) {
                dbHelper.deleteFromDetalleFactura(p.getId(), mesa.getIdFactura());
            }
            updateTable();
        }
    }

    @FXML
    public void importeBtn(){
        if(!productos.isEmpty()){
            if(mesa != null){
                dbHelper.generarImporte(mesa);
            }else{
                dbHelper.generarImporteSinMesa(productos);
                productos = FXCollections.observableArrayList();
                updateTable();
            }
        }
    }

    @FXML
    public void confirmarPago(){
        if(mesa != null){
            dbHelper.pagarFactura(mesa.getIdFactura());
            mesa = null;
            mesaSelected = false;
            numMesaTxtField.setText("");
            productos = FXCollections.observableArrayList();
            updateTable();
        }
    }

    @FXML
    public void historicoBtn(){
        final String INPUT_HISTORICO = "/Jaspersoft/FacturaHistoricoJS.jrxml";
        final String OUTPUT_HISTORICO = "src/FacturasHistoricos/Factura_";
        dbHelper.imprimirFactura(null, OUTPUT_HISTORICO+new java.util.Date(), INPUT_HISTORICO);
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
            stage.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isMesaSelected(){
        return mesaSelected;
    }

    public void setMesa(Mesa mesa){
        this.mesa = mesa;
        productos = mesa.getProductos();
        numMesaTxtField.setText(String.valueOf(mesa.getNum()));
        updateTable();
        mesaSelected = true;
    }

    public ObservableList<Producto> getProductos(){
        return productos;
    }
}
