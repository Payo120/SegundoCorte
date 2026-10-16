package me.edu.uam.segundocorte;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {

    private static final String CONFIG_FILE = "db.properties";

    public static Connection obtenerConexion() throws SQLException {
        Properties props = new Properties();
        Path path = Path.of(CONFIG_FILE);

        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile())) {
                props.load(fis);
            } catch (IOException e) {
                throw new SQLException("No se pudo leer el archivo de configuración db.properties.", "08001", e);
            }
        }

        String url = props.getProperty("db.url", System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/papeleria_campus"));
        String user = props.getProperty("db.user", System.getenv().getOrDefault("DB_USER", "postgres"));
        String password = props.getProperty("db.password", System.getenv().getOrDefault("DB_PASSWORD", ""));

        return DriverManager.getConnection(url, user, password);
    }
}