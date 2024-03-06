package org.example.modelo;

import java.util.LinkedList;

public class Mesa {
    private int num;
    private boolean ocupada;
    private int idFactura;
    private LinkedList<Producto> productos;

    public Mesa(int num, boolean ocupada) {
        this.num = num;
        this.ocupada = ocupada;
        idFactura = -1;
        productos = new LinkedList<>();
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

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public void addProducto(Producto p){
        productos.add(p);
    }
    public LinkedList<Producto> getProductos(){return productos;}
    public int getTotalArticulos(){
        return productos.size();
    }
}
