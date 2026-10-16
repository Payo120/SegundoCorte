package me.edu.uam.segundocorte;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {

    public static Connection obtenerConexion() throws SQLException {
        Properties props = new Properties();

        try (InputStream is = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new SQLException("No se encontró el archivo db.properties en resources.", "08001");
            }
            props.load(is);
        } catch (IOException e) {
            throw new SQLException("Error al leer db.properties", "08001", e);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");


        if (url == null || user == null || password == null) {
            throw new SQLException("Faltan propiedades en db.properties (db.url, db.user o db.password)", "08001");
        }

        return DriverManager.getConnection(url, user, password);
    }
}