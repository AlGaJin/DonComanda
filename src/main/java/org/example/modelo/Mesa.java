package org.example.modelo;

import java.util.LinkedList;

public class Mesa {
    private int num;
    private boolean ocupada;
    private LinkedList<Producto> productos;

    public Mesa(int num, boolean ocupada) {
        this.num = num;
        this.ocupada = ocupada;
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

    public void addProducto(Producto p){
        productos.add(p);
    }

    public int getTotalArticulos(){
        return productos.size();
    }
}
