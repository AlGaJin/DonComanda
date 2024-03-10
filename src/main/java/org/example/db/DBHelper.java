package org.example.db;

import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.example.modelo.Mesa;
import org.example.modelo.Producto;

import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * Clase que permite hacer cambios en la base de datos con ayuda de JDBC
 */
public class DBHelper {
    //Ruta del .jrxml para generar facturas simplificadas
    private final String INPUT_FACTSIMPLIFICADA = "/Jaspersoft/FacturaSimplificadaJS.jrxml";
    //Ruta y nombre con el que se va a guardar la factura simplificada
    private final String OUTPUT_FACTSIMPLIFICADA = "src/FacturasSimplificadas/Factura_";
    private Connection c; //Conexión con la base de datos
    private PreparedStatement ps; //Permite las acciones CRUD con la base de datos

    /**
     * Constructor sin parámetros que inicializa una conexión con la base de datos
     */
    public DBHelper(){
        String db = "DonComanda"; // Nombre de la base de datos
        String host = "localhost"; // Host de la base de datos
        String port = "3306"; // Puerto dedicado a la base de datos
        String urlConnection = "jdbc:mysql://"+host+":"+port+"/"+db; //Url que establece la conexión con la base de datos
        String user = "root"; //Usuario de la base de datos
        String pwd = "Lego"; //Contraseña de la base de datos

        try{
            c = DriverManager.getConnection(urlConnection, user, pwd);
            c.setAutoCommit(false);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera los productos que tenía una mesa asociada y se los añade a su lista
     * @param mesa La mesa de la que se quiere obtener los productos
     */
    private void setProductos(Mesa mesa){
        try{
            //Se comprueba que tenga una factura sin pagar
            ps = c.prepareStatement("SELECT id FROM facturas WHERE mesa=? AND pagada=false ORDER BY fecha DESC LIMIT 1");
            ps.setInt(1, mesa.getNum());
            ResultSet rs = ps.executeQuery();

            if(rs.next()){//Si tiene una factura sin pagar:

                int id_factura = rs.getInt("id"); //Se obtiene el identificador de la factura
                mesa.setIdFactura(id_factura); //Se setea en la mesa

                //Consulta para obtener los productos pedidos
                ps = c.prepareStatement("SELECT * FROM detalle_factura WHERE id_factura=?");
                ps.setInt(1, id_factura);
                rs = ps.executeQuery();

                while (rs.next()){ //Mientras haya tuplas en la consulta, se irán creando productos y añadiendo a la lista de la mesa
                    int id_producto = rs.getInt("id_producto");
                    int cantidad = rs.getInt("cantidad");
                    ps = c.prepareStatement("SELECT * FROM productos WHERE id=?");
                    ps.setInt(1, id_producto);
                    ResultSet rs2 = ps.executeQuery();
                    rs2.next();

                    int id_pdt = rs2.getInt("id");
                    String nombre = rs2.getString("nombre");
                    Double precio = rs2.getDouble("precio");
                    int dto = rs2.getInt("dto");

                    mesa.addProducto(new Producto(id_pdt, cantidad, nombre, precio, dto));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Permite obtener las mesas que hay en base de datos con los productos que hayan anotado a su cuenta
     * @return La lista de mesas que hay en base de datos con los productos que tengan anotados a su cuenta
     */
    public LinkedList<Mesa> getMesas(){
        try{
            LinkedList<Mesa> mesas = new LinkedList<>();
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM mesas");
            while (rs.next()){
                int numMesa = rs.getInt("num");
                boolean ocupada = rs.getBoolean("ocupada");
                Mesa mesa = new Mesa(numMesa, ocupada);
                setProductos(mesa);
                mesas.add(mesa);
            }
            return mesas;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Permite obtener un producto de la base de datos con el nombre que lo identifica
     * @param nombre Nombre del producto que se quiere obtener
     * @return Un producto con los datos básicos que identifican a un producto, o null en el caso de que se produzca un error inesperado
     */
    public Producto getProducto(String nombre) {
        try{
            ps = c.prepareStatement("SELECT * FROM productos WHERE nombre=?");
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            rs.next();

            int id = rs.getInt("id");
            double precio = rs.getDouble("precio");
            int dto = rs.getInt("dto");

            return new Producto(id, nombre, precio, dto);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cambia el estado de la mesa en la base de datos
     * @param numMesa Número identificativo de la mesa
     * @param ocupada El estado al que se quiere poner la mesa (True si está ocupada, False si no lo está)
     */
    public void updateMesaOcupada(int numMesa, boolean ocupada){
        try{
            ps = c.prepareStatement("UPDATE mesas SET ocupada =? WHERE num=?");
            ps.setBoolean(1, ocupada);
            ps.setInt(2, numMesa);
            ps.executeUpdate();
            c.commit();
        }catch (Exception e){
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Genera una nueva factura en base de datos para la mesa, puede ser null si la factura no es para una mesa
     * @param m Mesa a la que se va a relacionar la nueva factura
     * @return El número identificador de la factura, o null si se produce un error inesperado
     */
    public Integer generarFactura(Mesa m){
        Integer mesaNum = null;
        if(m != null){
            mesaNum = m.getNum();
        }

        try {
            ps = c.prepareStatement("INSERT INTO facturas(mesa, fecha) VALUES(?,?)");
            if(mesaNum == null){
                ps.setNull(1, Types.INTEGER);
            }else{
                ps.setInt(1, mesaNum);
            }
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            c.commit();


            if(mesaNum == null){
                ps = c.prepareStatement("SELECT id FROM facturas WHERE mesa is null ORDER BY fecha DESC LIMIT 1");
            }else{
                ps = c.prepareStatement("SELECT id FROM facturas WHERE mesa = ? ORDER BY fecha DESC LIMIT 1");
                ps.setInt(1, mesaNum);
            }
            ResultSet rs = ps.executeQuery();
            rs.next();

            return rs.getInt("id");
        }catch (Exception e){
            e.printStackTrace();
            try{
                c.rollback();
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Permite generar los datos en la tabla de la base de datos para indicar los productos y las cantidades
     * que se han pedido de dicho producto en una factura concreta
     * @param productos La lista de productos que se quieren añadir a la base de datos
     * @param idFactura El identificador de la factura a la que estará relacionada la tabla
     */
    public void crearFacturaDetalle(ObservableList<Producto> productos, int idFactura){
        try{
            ps = c.prepareStatement("INSERT INTO detalle_factura VALUES (?,?,?)");
            for (Producto p: productos) {
                ps.setInt(1, idFactura);
                ps.setInt(2, p.getId());
                ps.setInt(3, p.getUds());
                ps.executeUpdate();
            }
            c.commit();
        }catch (Exception e) {
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Genera un importe para un pedido sin mesa asignada, en este caso se imprime la factura y se paga al instante
     * @param productos La lista de productos que se han pedido
     */
    public void generarImporteSinMesa(ObservableList<Producto> productos){
        int idFactura = generarFactura(null); //Se genera la factura para el pedido
        try{
            crearFacturaDetalle(productos, idFactura); //Se añaden los productos con la factura generada

            //Se crea la factura simplificada
            generarImporte(idFactura);

            //Y se paga la factura
            pagarFactura(idFactura);
        }catch (Exception e) {
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Genera la factura simplificada con JasperReport
     * @param idFactura el id de la factura que se quiere imprimir
     */
    public void generarImporte(int idFactura){
        try{
            Map<String, Object> map = new HashMap<>();
            map.put("id_factura", idFactura);
            imprimirFactura(map,OUTPUT_FACTSIMPLIFICADA+ idFactura , INPUT_FACTSIMPLIFICADA);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cambia el estado de una factura a "pagado", si hay una mesa relacionada cambia su estado a no ocupada
     * @param idFactura El identificador de la factura que se quiere pagar
     */
    public void pagarFactura(int idFactura){
        try{
            ps = c.prepareStatement("UPDATE facturas SET pagada = true WHERE id=?");
            ps.setInt(1,idFactura);
            ps.executeUpdate();
            c.commit();

            ps = c.prepareStatement("SELECT mesa FROM facturas WHERE id = ?");
            ps.setInt(1, idFactura);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int numMesa = rs.getInt("mesa");
            if(numMesa > 0){
               updateMesaOcupada(numMesa, false);
            }
        }catch (Exception e){
            e.printStackTrace();
            try{
                c.rollback();
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * Cambia la cantidad pedida de un producto en la tabla de detalle_producto
     * @param idProducto El producto al que se le quiere cambiar la cantidad pedida
     * @param idFactura La factura a la que está relacionada el producto
     * @param cantidad La cantidad que se le quiere aplicar
     */
    public void updateCantidad(int idProducto, int idFactura, int cantidad){
        try{
            ps = c.prepareStatement("UPDATE detalle_factura SET cantidad = ? WHERE id_producto = ? AND id_factura = ?");
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.setInt(3, idFactura);
            ps.executeUpdate();
            c.commit();
        }catch (Exception e){
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Método que añade un producto a la tabla detalle_factura, si ya está insertada en la tabla aumentará
     * su cantidad pedida en uno
     * @param idProducto El identificador del producto que se va a añadir, o aumentar las unidades pedidas, a la tabla
     * @param idFactura La factura a la que está realcionada el producto
     */
    public void addToDetalleFactura(int idProducto, int idFactura){
        try{
            c.rollback(); //Este rollback sin sentido soluciona, por algún motivo, un bug (no tocar)
            ps = c.prepareStatement("SELECT cantidad FROM detalle_factura WHERE id_factura = ? AND id_producto = ?");
            ps.setInt(1, idFactura);
            ps.setInt(2, idProducto);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                updateCantidad(idProducto, idFactura, rs.getInt("cantidad")+1);
            }else{
                ps = c.prepareStatement("INSERT INTO detalle_factura(id_factura, id_producto) VALUES(?,?)");
                ps.setInt(1, idFactura);
                ps.setInt(2, idProducto);
                ps.executeUpdate();
                c.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Elimina un producto que se había pedido con anterioridad
     * @param idProducto El identificador del producto que se quiere eliminar
     * @param idFactura El identificador de la factura a la que está relacionado el producto
     */
    public void deleteFromDetalleFactura(int idProducto, int idFactura) {
        try{
            ps = c.prepareStatement("DELETE FROM detalle_factura WHERE id_factura = ? AND id_producto = ?");
            ps.setInt(1, idFactura);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
            c.commit();
        }catch (Exception e){
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Genera un .pdf con el estilo .jrxml indicado
     * @param map Contiene los parámetros que se le quiera pasa al JasperReport
     * @param output La ruta donde se va a guardar el pdf generado
     * @param input La ruta del .jrxml que se va a usar para generar el pdf
     */
    public void imprimirFactura(Map<String, Object> map, String output, String input){
        try{
            InputStream reportFile = getClass().getResourceAsStream(input);
            JasperReport jr = JasperCompileManager.compileReport(reportFile);
            JasperPrint jp = JasperFillManager.fillReport(jr, map, c);
            JRPdfExporter export = new JRPdfExporter();
            export.setExporterInput(new SimpleExporterInput(jp));
            export.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(output+".pdf")));
            SimplePdfExporterConfiguration config = new SimplePdfExporterConfiguration();
            export.setConfiguration(config);
            export.exportReport();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
