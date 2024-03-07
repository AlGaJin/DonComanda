package org.example.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

public class Mesa {
    private int num;
    private boolean ocupada;
    private int idFactura;
    private ObservableList<Producto> productos;

    public Mesa(int num, boolean ocupada) {
        this.num = num;
        this.ocupada = ocupada;
        idFactura = -1;
        productos = FXCollections.observableArrayList();
    }

    public int getNum() {
        return num;
    }
    public boolean isOcupada() {
        return ocupada;
    }
    public void changeOcupada(){
        ocupada = !ocupada;
    }
    public int getIdFactura() {
        return idFactura;
    }
    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }
    public void addProducto(Producto p){
        productos.add(p);
    }
    public ObservableList<Producto> getProductos(){return productos;}
    public void setProductos(ObservableList<Producto> productos){
        this.productos = productos;
    }
    public int getTotalArticulos(){
        return productos.size();
    }
}
