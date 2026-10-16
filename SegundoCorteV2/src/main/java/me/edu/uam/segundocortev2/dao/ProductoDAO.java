package me.edu.uam.segundocortev2.dao;

import me.edu.uam.segundocortev2.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductoDAO {


    public List<Producto> listarTodos() throws SQLException {
        return buscarConFiltros("", "Todas", false);
    }


    public List<Producto> buscarConFiltros(String texto, String categoria, boolean soloConExistencias) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id_producto, codigo, nombre, categoria, precio, stock " +
                        "FROM producto WHERE 1=1"
        );

        List<Object> parametros = new ArrayList<>();


        if (texto != null && !texto.trim().isEmpty()) {
            sql.append(" AND (codigo ILIKE ? OR nombre ILIKE ?)");
            String patron = "%" + texto.trim() + "%";
            parametros.add(patron);
            parametros.add(patron);
        }


        if (categoria != null && !categoria.trim().isEmpty() && !categoria.equalsIgnoreCase("Todas")) {
            sql.append(" AND categoria = ?");
            parametros.add(categoria.trim());
        }


        if (soloConExistencias) {
            sql.append(" AND stock > 0");
        }


        sql.append(" ORDER BY codigo ASC");

        List<Producto> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            registrarErrorSQL("CONSULTA / FILTRO DE PRODUCTOS", e);
            throw e;
        }

        return lista;
    }


    public boolean existeCodigo(String codigoNormalizado, Integer idExcluir) throws SQLException {
        String sql = (idExcluir == null)
                ? "SELECT 1 FROM producto WHERE UPPER(BTRIM(codigo)) = UPPER(BTRIM(?))"
                : "SELECT 1 FROM producto WHERE UPPER(BTRIM(codigo)) = UPPER(BTRIM(?)) AND id_producto <> ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoNormalizado);
            if (idExcluir != null) {
                ps.setInt(2, idExcluir);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            registrarErrorSQL("VERIFICAR DUPLICIDAD DE CODIGO", e);
            throw e;
        }
    }


    public int insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (codigo, nombre, categoria, precio, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setBigDecimal(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());

            return ps.executeUpdate();
        } catch (SQLException e) {
            registrarErrorSQL("INSERTAR PRODUCTO", e);
            throw e;
        }
    }


    public int actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ? " +
                "WHERE id_producto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setBigDecimal(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getIdProducto());

            return ps.executeUpdate();
        } catch (SQLException e) {
            registrarErrorSQL("ACTUALIZAR PRODUCTO (ID=" + producto.getIdProducto() + ")", e);
            throw e;
        }
    }


    public int eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate();
        } catch (SQLException e) {
            registrarErrorSQL("ELIMINAR PRODUCTO (ID=" + idProducto + ")", e);
            throw e;
        }
    }


    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getInt("id_producto"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("categoria"),
                rs.getBigDecimal("precio"),
                rs.getInt("stock")
        );
    }


    public static void registrarErrorSQL(String operacion, SQLException e) {
        System.err.printf("[DIAGNÓSTICO SQL] Operación: %s | SQLState: %s | Código Error: %d | Detalle: %s%n",
                operacion,
                e.getSQLState() != null ? e.getSQLState() : "N/A",
                e.getErrorCode(),
                e.getMessage());
    }
}