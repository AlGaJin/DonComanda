package org.example.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase que recoge los diferentes datos necesarios para identificar una mesa
 */
public class Mesa {
    private int num; //El número de mesa que la identifica unitariamente
    private boolean ocupada; //Indica si está ocupada la mesa
    private int idFactura; //Factura a la que está asociada la mesa en la base de datos
    private ObservableList<Producto> productos; //Productos que se han pedido en la mesa

    /**
     * Constructor parametrizado para instanciar una mesa con dos datos esenciales
     * @param num número que identifica la mesa
     * @param ocupada booleano que indica si está ocupada
     */
    public Mesa(int num, boolean ocupada) {
        this.num = num;
        this.ocupada = ocupada;
        idFactura = -1;
        productos = FXCollections.observableArrayList();
    }

    /**
     * Permite obtener el número de mesa
     * @return el número de mesa
     */
    public int getNum() {
        return num;
    }

    /**
     * Permite conocer si la mesa está ocupada
     * @return true en el caso de que esté ocupada, y false en el caso de que no esté ocupada
     */
    public boolean isOcupada() {
        return ocupada;
    }

    /**
     * Permite obtener el id de factura que tiene asociada la mesa
     * @return el id de la factura asociada a la mesa (-1 si no tiene factura asociada)
     */
    public int getIdFactura() {
        return idFactura;
    }

    /**
     * Permite cambiar el id de la factura que tiene la mesa asociada
     * @param idFactura id de la factura asociada a la mesa
     */
    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    /**
     * Permite añadir un producto a la lista de productos de la mesa
     * @param p el Producto que se desea añadir a la lista de la mesa
     */
    public void addProducto(Producto p){
        productos.add(p);
    }

    /**
     * Permite obtener los productos que tiene la mesa asociados
     * @return el ObserbableList de productos que tiene la mesa asociado
     */
    public ObservableList<Producto> getProductos(){
        return productos;
    }

    /**
     * Permite cambiar la lista de productos que tiene la mesa asociados
     * @param productos la nueva lista de productos que va a tener la mesa
     */
    public void setProductos(ObservableList<Producto> productos){
        this.productos = productos;
    }
}
