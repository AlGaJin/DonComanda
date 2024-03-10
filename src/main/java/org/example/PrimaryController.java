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
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Controlador de la vista principal que da funcionalidad a los diferentes elementos de la vista
 */
public class PrimaryController {

    @FXML
    private TableView<Producto> tablaPedido; //Tabla que contendrá los productos que se vayan pidiendo para mostar sus datos
    @FXML
    private GridPane categoriasGridPane; //Contiene los diferentes botones de las diferentes categorías
    @FXML
    private GridPane productosGridPane; //Contiene los diferentes botones de los diferentes productos
    @FXML
    private TextField numMesaTxtField, calcTxtField; //Campos que contiene datos editables
    @FXML
    private Label articulosLabel, udsLabel, totalLabel; //Etiquetas para mostrar diferentes datos de forma dinámica
    private boolean mesaSelected; //Permite conocer si hay o no una mesa seleccionada
    private final DBHelper dbHelper = new DBHelper(); //Permite la conexión con la base de datos
    private ObservableList<Producto> productos; //Lista de productos que se han pedido
    private Mesa mesa; //Mesa a la que pertenece el pedido (puede estar a null si se pide fuera de mesa)

    /**
     * Se llama al inicializar el controlador y carga datos necesarios para el funcionamiento del programa
     */
    public void initialize(){
        mesaSelected = false;
        productos = FXCollections.observableArrayList();
        cargarCategorias();
        generarTabla();
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
     * Carga las categorías en la vista
     */
    private void cargarCategorias(){
        ObservableList<Node> children = categoriasGridPane.getChildren(); //Se obtienen todos los nodos hijos del grid
        for (Node vbox :children) { //Esos nodos son VBox, de los cuáles se van a recoger los nodos que contienen es decir:
            ObservableList<Node> vboxChildren = ((VBox)vbox).getChildren();
            String catName = ((Label)vboxChildren.get(1)).getText().toLowerCase(); //El Label que indica la categoría que es
            Button catBtn = ((Button) vboxChildren.get(0)); // El botón que permite darle funcionalidad

            //Con el nombre de la categoría se obtiene la ruta de la imagen
            String imgPath = "file:src/main/resources/Imagenes/Productos/"+catName + ".png";

            setBkgImg(imgPath, catBtn); //Se aplica la imagen al botón

            catBtn.setOnAction(event -> selecCategoria(catName)); //Se aplica acción al botón
        }
    }

    /**
     * Dependiendo la categoría que se seleccione mostrará unos productos u otros
     * @param catName Nombre de la categoría seleccionada
     */
    private void selecCategoria(String catName){
        File dir = new File("src/main/resources/Imagenes/Productos/"+catName); //Se obtiene la carpeta de la categoría
        File imagenes[] = dir.listFiles(); //Se listan todos los archivos que contiene la categoría

        ObservableList<Node> childen = productosGridPane.getChildren(); //Se obtienen los nodos del grid que contiene los botones
        int i = 0; //Contador para saber si hay más botones que productos

        for(Node vbox: childen){ //Los nodos son VBox, de los cuales se obtiene los siguientes nodos:
            ObservableList<Node> vboxChildren = ((VBox) vbox).getChildren();
            if(i < imagenes.length){ //Si hay más VBox que imágenes se ocultan los que no se utilicen
                vbox.setVisible(true);
                for(Node node: vboxChildren){
                    if(node instanceof Label){
                        ((Label)node).setText(imagenes[i].getName().split("\\.")[0]); //Se aplica al label el nombre del producto
                    }else if(node instanceof Button){
                        setBkgImg("file:" + imagenes[i].getPath(), (Button) node); //Se aplica al botón la imagen del producto
                    }
                }
            }else{
                vbox.setVisible(false);
            }
            i++;
        }
    }

    /**
     * Da acción a los botones de los productos, de tal forma que dependiendo del que se eliga se agregará a la lista
     * de productos y a la base de datos si hay una mesa seleccionada
     * @param e Con él se obtiene el botón que lo acciona
     */
    @FXML
    public void selecProducto(Event e){
        //Se obtiene el nombre del producto a través del botón que lo acciona y el VBox en el que está contenido
        String nombreSelec = ((Label)((Button)e.getSource()).getParent().getChildrenUnmodifiable().get(1)).getText().toLowerCase();
        boolean enc = false; //Para saber si el producto ya se encontraba dentro de la lista
        if(!productos.isEmpty()){
            for (Producto p: productos) {
                if(p.getNombre().equals(nombreSelec)){ //Si el producto ya estaba contenido se aumenta en uno la cantidad de unidades pedidas
                    p.aumentarUds();
                    enc = true;
                    break;
                }
            }
        }

        if(!enc){ //Si no se ha encontrado se añade a la lista como un nuevo producto
            productos.add(dbHelper.getProducto(nombreSelec));
        }

        if(mesaSelected){ //Si hay una mesa seleccionada se añade a la factura de la base de datos
            dbHelper.addToDetalleFactura(dbHelper.getProducto(nombreSelec).getId(), mesa.getIdFactura());
        }
        updateTable(); //Se actualizan los datos de la tabla
    }

    /**
     * Crea las diferentes columnas que se van a mostrar en la tabla, así como indicar de qué tipo y de dónde se
     * van a recoger los datos a mostrar
     */
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

    /**
     * Actualiza los datos de la tabla
     */
    public void updateTable(){
        if(mesaSelected){ //Si hay una mesa seleccionada se actualzian los productos que tiene relacionados
            mesa.setProductos(productos);
        }
        tablaPedido.getItems().setAll(productos); //Se cambian los productos que tiene la tabla asociados

        //Se actualizan los datos totales
        int articulos = productos.size(); //Total de artículos distintos
        int uds = 0; //Total de unidades pedidas
        double total = 0; //Precio total pedido
        for (Producto p: productos) {
            uds += p.getUds();
            total += p.getTotal();
        }
        total = (double) Math.round(total*100)/100; //Formatea el número para que se muestren como máximo dos decimales

        articulosLabel.setText(String.valueOf(articulos));
        udsLabel.setText(String.valueOf(uds));
        totalLabel.setText(String.valueOf(total));
    }

    /**
     * Permite dar acción al botón de la vista que preparará la aplicación para crear una factura
     * sin tener una mesa seleccionada
     */
    @FXML
    public void facturaSinMesaBtn(){
        mesa = null;
        numMesaTxtField.setText("");
        productos = FXCollections.observableArrayList();
        mesaSelected = false;
        updateTable();
    }

    /**
     * Permite dar acción a los botones de la calculadora
     * @param e Con él se obtiene el botón que lo acciona
     */
    @FXML
    public void calculadora(Event e){
        String number = ((Button)e.getSource()).getText(); //Se obtiene el valor que tiene como texto el botón
        if(number.equals("C")){ //Si se pulsa C se borra lo escrito en el textfield
            calcTxtField.setText("");
        }else{
            calcTxtField.appendText(number); //Sino se agrega el número al textfield
        }
    }

    /**
     * Da acción al botón intro de la calculadora, de tal manera que si hay un objeto de la tabla seleccionado
     * y hay un número escrito, se cambiará al número de unidades pedidas del producto seleccionado
     */
    @FXML
    public void introBtn(){
        String uds = calcTxtField.getText();
        int idx = tablaPedido.getSelectionModel().getSelectedIndex();
        if(!uds.isEmpty() && idx != -1){
            if(Integer.parseInt(uds) == 0){ //Si el número que hay en el textfield es 0 se elimina el producto
                eliminarBtn();
            }else {
                productos.get(idx).setUds(Integer.parseInt(uds));
                if(mesaSelected){
                    dbHelper.updateCantidad(productos.get(idx).getId(), mesa.getIdFactura(), Integer.parseInt(uds));
                }
            }
            calcTxtField.setText("");
            updateTable();
        }
    }

    /**
     * Elimina un producto de la tabla y, si hay una mesa seleccionada, de la factura de la mesa en la base de datos
     */
    @FXML
    public void eliminarBtn(){
        int idx = tablaPedido.getSelectionModel().getSelectedIndex();
        if(idx != -1){
            Producto p = productos.remove(idx);
            if(mesaSelected) {
                dbHelper.deleteFromDetalleFactura(p.getId(), mesa.getIdFactura());
            }
            updateTable();
        }
    }

    /**
     * Genera el importe, la factura simplificada de lo pedido con JasperReport
     */
    @FXML
    public void importeBtn(){
        if(!productos.isEmpty()){
            if(mesaSelected){
                dbHelper.generarImporte(mesa.getIdFactura());
            }else{
                dbHelper.generarImporteSinMesa(productos);
                productos = FXCollections.observableArrayList();
                updateTable();
            }
        }
    }

    /**
     * Si hay una mesa seleccionada confirma el pago en la base de datos y limpia la vista
     */
    @FXML
    public void confirmarPago(){
        if(mesaSelected){
            dbHelper.pagarFactura(mesa.getIdFactura());
            facturaSinMesaBtn();
        }
    }

    /**
     * Genera el histórico con JasperSoft del día actual
     */
    @FXML
    public void historicoBtn(){
        final String INPUT_HISTORICO = "/Jaspersoft/FacturaHistoricoJS.jrxml"; //Indica la ruta del .jrxml
        final String OUTPUT_HISTORICO = "src/FacturasHistoricos/Factura_"; //Indica la ruta y el nombre con el que se va a guardar el histórico
        String fechaActual = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
        dbHelper.imprimirFactura(null, OUTPUT_HISTORICO+fechaActual, INPUT_HISTORICO);
    }

    /**
     * Abre una nueva ventana que permite seleccionar una mesa
     */
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

    /**
     * Permite conocer si hay una mesa seleccionada
     * @return true si hay una mesa seleccionada, false si no la hay
     */
    public boolean isMesaSelected(){
        return mesaSelected;
    }

    /**
     * Permite cambiar la mesa seleccionada y con ello elementos de la vista
     * @param mesa La nueva mesa que se selecciona
     */
    public void setMesa(Mesa mesa){
        this.mesa = mesa;
        productos = mesa.getProductos();
        numMesaTxtField.setText(String.valueOf(mesa.getNum()));
        updateTable();
        mesaSelected = true;
    }

    /**
     * Permite obtener la lista de productos que hay generados para la cuenta actual
     * @return La lista de productos de la cuenta actual
     */
    public ObservableList<Producto> getProductos(){
        return productos;
    }
}
