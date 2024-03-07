package org.example.db;

import javafx.collections.ObservableList;
import org.example.modelo.Mesa;
import org.example.modelo.Producto;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DBHelper {
    private Connection c;
    private PreparedStatement ps;

    public DBHelper(){
        String db = "DonComanda";
        String host = "localhost";
        String port = "3306";
        String urlConnection = "jdbc:mysql://"+host+":"+port+"/"+db;
        String user = "root";
        String pwd = "Lego";

        try{
            c = DriverManager.getConnection(urlConnection, user, pwd);
            c.setAutoCommit(false);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setProductos(Mesa mesa){
        try{
            ps = c.prepareStatement("SELECT id FROM facturas WHERE mesa=? AND pagada=false ORDER BY fecha DESC LIMIT 1");
            ps.setInt(1, mesa.getNum());
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                int id_factura = rs.getInt("id");
                mesa.setIdFactura(id_factura);
                ps = c.prepareStatement("SELECT * FROM detalle_factura WHERE id_factura=?");
                ps.setInt(1, id_factura);
                rs = ps.executeQuery();

                while (rs.next()){
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

    public void updateMesaOcupada(int idMesa, boolean ocupada){
        try{
            ps = c.prepareStatement("UPDATE mesas SET ocupada =? WHERE num=?");
            ps.setBoolean(1, ocupada);
            ps.setInt(2, idMesa);
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

    public void generarImporte(ObservableList<Producto> productos){
        int idFactura = generarFactura(null);
        try{
            for (Producto p: productos) {
                ps = c.prepareStatement("INSERT INTO detalle_factura VALUES (?,?,?)");
                ps.setInt(1, idFactura);
                ps.setInt(2, p.getId());
                ps.setInt(3, p.getUds());
                ps.executeUpdate();
                c.commit();
            }

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

    public void generarImporte(Mesa m){
        try{
            for (Producto p: m.getProductos()) {
                ps = c.prepareStatement("INSERT INTO detalle_factura VALUES (?,?,?)");
                ps.setInt(1, m.getIdFactura());
                ps.setInt(2, p.getId());
                ps.setInt(3, p.getUds());
                ps.executeUpdate();
                c.commit();
            }
        }catch (Exception e) {
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void pagarFactura(int idFactura){
        try{
            ps = c.prepareStatement("UPDATE facturas SET pagada = true WHERE id=?");
            ps.setInt(1,idFactura);
            ps.executeUpdate();
            c.commit();
        }catch (Exception e){
            e.printStackTrace();
            try{
                c.rollback();
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }

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
}
