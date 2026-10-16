package me.edu.uam.segundocortev2.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnection {

    private static final String CONFIG_FILE = "config.properties";

    private DatabaseConnection() {

    }


    public static Connection getConnection() throws SQLException {
        Properties props = cargarPropiedades();

        String url = obtenerValor(props, "db.url", "DB_URL");
        String user = obtenerValor(props, "db.user", "DB_USER");
        String password = obtenerValor(props, "db.password", "DB_PASSWORD");

        if (url == null || url.isBlank() || user == null || user.isBlank() || password == null) {
            throw new SQLException(
                    "Configuración de base de datos incompleta. Verifique 'config.properties' (db.url, db.user, db.password).",
                    "08001"
            );
        }

        DriverManager.setLoginTimeout(5);
        return DriverManager.getConnection(url.trim(), user.trim(), password);
    }

    private static Properties cargarPropiedades() throws SQLException {
        Properties props = new Properties();
        Path rutaConfig = Paths.get(CONFIG_FILE);

        if (Files.exists(rutaConfig)) {
            try (InputStream input = new FileInputStream(rutaConfig.toFile())) {
                props.load(input);
                return props;
            } catch (IOException e) {
                throw new SQLException("No se pudo leer el archivo " + CONFIG_FILE + ": " + e.getMessage(), "08001", e);
            }
        } else {
            if (System.getenv("DB_URL") == null) {
                throw new SQLException(
                        "No se encontró el archivo '" + CONFIG_FILE + "' en la raíz del proyecto. " +
                                "Copie 'config.example.properties' como 'config.properties' y configure sus credenciales.",
                        "08001"
                );
            }
            return props;
        }
    }

    private static String obtenerValor(Properties props, String propKey, String envKey) {
        String valor = props.getProperty(propKey);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(envKey);
        }
        return valor;
    }
}