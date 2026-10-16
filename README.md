# Catálogo de Productos – Papelería Campus (JavaFX + PostgreSQL + JDBC)

Aplicación de escritorio desarrollada en JavaFX que almacena y gestiona el catálogo de productos de la papelería Campus conectándose a una base de datos relacional PostgreSQL mediante JDBC.

## Autor y Entorno
- **Estudiante:** Rafael Amador Pérez
- **Tecnologías:** Java 21, JavaFX 21.0.2, Maven, PostgreSQL 18, JDBC (pgJDBC 42.7.4).
- **IDE:** IntelliJ IDEA.

## Estructura del Proyecto
- `src/main/java/me/edu/uam/segundocortev2/` -> Clases principales, controlador, modelo y DAO.
- `src/main/resources/me/edu/uam/segundocortev2/` -> Interfaz gráfica en FXML (`hello-view.fxml`).
- `sql/estructura.sql` -> Script de creación de la tabla `producto` con restricciones CHECK, UNIQUE, NOT NULL e IDENTITY.
- `sql/datos.sql` -> Script con los 12 registros obligatorios iniciales.

## Configuración y Ejecución
1. **Base de Datos:** Cree una base de datos en PostgreSQL llamada `papeleria_campus`. Ejecute primero el script `sql/estructura.sql` y luego `sql/datos.sql` en pgAdmin.
2. **Conexión Externa:** Copie el archivo `config.example.properties` como `config.properties` en la raíz del proyecto y configure sus credenciales de acceso locales (URL, usuario y contraseña).
3. **Ejecución:** Abra el proyecto en IntelliJ IDEA y ejecute la clase `Launcher.java`.
