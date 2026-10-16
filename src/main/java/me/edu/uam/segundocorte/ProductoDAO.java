package me.edu.uam.segundocorte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> buscarProductos(String textoBusqueda, String categoria, boolean soloConExistencias) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id_producto, codigo, nombre, categoria, precio, stock FROM producto WHERE 1=1"
        );
        List<Object> parametros = new ArrayList<>();

        if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
            sql.append(" AND (codigo ILIKE ? OR nombre ILIKE ?)");
            String patron = "%" + textoBusqueda.trim() + "%";
            parametros.add(patron);
            parametros.add(patron);
        }

        if (categoria != null && !categoria.trim().isEmpty() && !"Todas".equalsIgnoreCase(categoria.trim())) {
            sql.append(" AND categoria = ?");
            parametros.add(categoria.trim());
        }

        if (soloConExistencias) {
            sql.append(" AND stock > 0");
        }

        sql.append(" ORDER BY codigo ASC");

        List<Producto> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock")
                    );
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            registrarDiagnostico("CONSULTA_PRODUCTOS", e);
            throw e;
        }
        return lista;
    }

    public boolean existeCodigoEnOtroProducto(String codigoNormalizado, Integer idExcluir) throws SQLException {
        String sql = (idExcluir == null)
                ? "SELECT COUNT(*) FROM producto WHERE UPPER(BTRIM(codigo)) = ?"
                : "SELECT COUNT(*) FROM producto WHERE UPPER(BTRIM(codigo)) = ? AND id_producto <> ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoNormalizado);
            if (idExcluir != null) {
                ps.setInt(2, idExcluir);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            registrarDiagnostico("VERIFICAR_CODIGO_DUPLICADO", e);
            throw e;
        }
        return false;
    }

    public int insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (codigo, nombre, categoria, precio, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setBigDecimal(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());

            return ps.executeUpdate();
        } catch (SQLException e) {
            registrarDiagnostico("INSERTAR_PRODUCTO", e);
            throw e;
        }
    }

    public int actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ? WHERE id_producto = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setBigDecimal(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getIdProducto());

            return ps.executeUpdate();
        } catch (SQLException e) {
            registrarDiagnostico("ACTUALIZAR_PRODUCTO", e);
            throw e;
        }
    }

    public int eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate();
        } catch (SQLException e) {
            registrarDiagnostico("ELIMINAR_PRODUCTO", e);
            throw e;
        }
    }

    public static void registrarDiagnostico(String operacion, SQLException e) {
        System.err.printf("[DIAGNÓSTICO SQL] Operación: %s | SQLState: %s | ErrorCode: %d%n",
                operacion, e.getSQLState(), e.getErrorCode());
    }
}