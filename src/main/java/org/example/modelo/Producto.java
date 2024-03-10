package org.example.modelo;

/**
 * Clase que recoge los diferentes datos necesarios para identificar un producto
 */
public class Producto {
    private int id; //Número que identifica al producto de forma unitaria
    private int uds; //El total de unidades que se han pedido del producto
    private String nombre; //Nombre identificativo del producto
    private Double precio; //Coste original del producto
    private int dto; //Descuento que tiene le producto
    private Double total; //Total calculado con el número de unidades y el dto. aplicado

    /**
     * Constructor parametrizado que permite instanciar un producto con los datos esenciales
     * @param id Número identificativo del producto en base de datos
     * @param nombre Nombre identificatibbo del producto
     * @param precio Valor original del producto
     * @param dto Descuento que se le aplica al producto
     */
    public Producto(int id, String nombre, Double precio, int dto){
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.dto = dto;
        uds = 1;
        calcularTotal();
    }

    /**
     * Constructor parametrizado que permite instanciar un producto con todos los datos
     * @param id Número identificativo del producto en base de datos
     * @param uds Total de productos que se han pedido del mismo producto
     * @param nombre Nombre identificatibbo del producto
     * @param precio Valor original del producto
     * @param dto Descuento que se le aplica al producto
     */
    public Producto(int id, int uds, String nombre, Double precio, int dto) {
        this.id = id;
        this.uds = uds;
        this.nombre = nombre;
        this.precio = precio;
        this.dto = dto;
        calcularTotal();
    }

    /**
     * Permite obtener el id que identifica al producto en base de datos
     * @return Id que identifica al producto en base de datos
     */
    public int getId() {
        return id;
    }

    /**
     * Permite obtener el nombre del producto
     * @return El nombre del producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Permite obtener el número de unidades pedidas del producto
     * @return El número de unidades pedidas del producto
     */
    public int getUds() {
        return uds;
    }

    /**
     * Aumenta en uno la cantidad de unidades del objeto y recalcula el coste total
     */
    public void aumentarUds(){
        uds++;
        calcularTotal();
    }

    /**
     * Permite cambiar el número total de unidades pedidas del producto
     * @param uds El nuevo total de unidades que se ha pedido del producto
     */
    public void setUds(int uds){
        this.uds = uds;
        calcularTotal();
    }

    /**
     * Permite obtener el precio del producto
     * @return El precio del producto
     */
    public Double getPrecio() { //Usado por el CellFactory de la tabla
        return precio;
    }

    /**
     * Permite obtener el dto. aplicado al producto
     * @return El dto. que tiene el producto
     */
    public int getDto() { //Usado por el CellFactory de la tabla
        return dto;
    }

    /**
     * Permite obtener el precio total calculado con las uds. y el precio con dto.
     * @return El coste total del pedido del producto
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Calcula el precio total del producto teniendo en cuenta el precio, el dto. y las unidades pedidas
     */
    private void calcularTotal(){
        total = (double) Math.round(((precio-(precio*((double)dto/100)))*uds)*100)/100;
    }
}
