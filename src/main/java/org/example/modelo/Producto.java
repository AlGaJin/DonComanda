package org.example.modelo;

public class Producto {
    private int id;
    private int uds;
    private String nombre;
    private Double precio;
    private int dto;
    private Double total;

    public Producto(int id, String nombre, Double precio, int dto){
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.dto = dto;

        uds = 1;
        total = precio;
    }

    public void aumentarUds(){
        uds++;
    }

    public void setUds(int uds){
        this.uds = uds;
    }

    public void setPrecio(Double precio){
        this.precio = precio;
    }

    public void setDto(int dto){
        this.dto = dto;
    }

    private void calcularTotal(){
        total = (precio-(precio*(dto/100)))*uds;
    }
}
