package org.example.db;

import org.example.modelo.Mesa;
import org.example.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
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

    public List<Mesa> getMesas(){
        try{
            List<Mesa> mesas = new ArrayList<>();
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
}
