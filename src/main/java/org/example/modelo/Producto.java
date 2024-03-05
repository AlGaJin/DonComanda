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

    public Producto(int id, int uds, String nombre, Double precio, int dto) {
        this.id = id;
        this.uds = uds;
        this.nombre = nombre;
        this.precio = precio;
        this.dto = dto;
        calcularTotal();
    }

    public String getNombre() {
        return nombre;
    }

    public int getUds() {
        return uds;
    }

    public Double getPrecio() {
        return precio;
    }

    public int getDto() {
        return dto;
    }

    public Double getTotal() {
        return total;
    }

    public void aumentarUds(){
        uds++;
        calcularTotal();
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
