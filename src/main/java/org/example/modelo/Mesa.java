package org.example.modelo;

import java.util.LinkedList;

public class Mesa {
    private int num;
    private boolean ocupada;
    private int numComensales;
    private LinkedList<Producto> productos;

    public Mesa(int num, boolean ocupada, int numComensales) {
        this.num = num;
        this.ocupada = ocupada;
        this.numComensales = numComensales;
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

    public int getNumComensales() {
        return numComensales;
    }

    public void setNumComensales(int numComensales) {
        this.numComensales = numComensales;
    }

    public void addProducto(Producto p){
        productos.add(p);
    }

    public int getTotalArticulos(){
        return productos.size();
    }
}
