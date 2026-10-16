# Catálogo de Productos – Papelería Campus (JavaFX + PostgreSQL + JDBC)

Aplicación de escritorio desarrollada en Java 21 y JavaFX 21 que gestiona de forma integral el catálogo de productos de la papelería Campus. El sistema se conecta a una base de datos relacional PostgreSQL mediante JDBC nativo, implementando el patrón de diseño DAO (Data Access Object), consultas parametrizadas seguras, validaciones robustas y un control estricto de excepciones de negocio y de base de datos.

---

## 📋 Tabla de Contenidos
1. [Información General y Entorno](#-información-general-y-entorno)
2. [Arquitectura y Estructura del Proyecto](#-arquitectura-y-estructura-del-proyecto)
3. [Esquema de Base de Datos y Restricciones SQL](#-esquema-de-base-de-datos-y-restricciones-sql)
4. [Características Principales y Funcionalidades (CRUD)](#-características-principales-y-funcionalidades-crud)
5. [Sistema de Búsquedas, Filtros y Sincronización](#-sistema-de-búsquedas-filtros-y-sincronización)

---

## 🛠️ Información General y Entorno
- **Desarrollador / Estudiante:** Rafael Amador Pérez
- **Institución:** Universidad Americana (UAM) / Fundación Uno – CS50x.ni
- **Tecnologías Principales:** 
  - **Lenguaje:** Java (OpenJDK 21)
  - **Interfaz Gráfica:** JavaFX 21.0.2 (FXML + Scene Builder)
  - **Gestor de Dependencias y Compilación:** Apache Maven
  - **Base de Datos:** PostgreSQL 18
  - **Driver de Conexión:** PostgreSQL JDBC Driver (pgJDBC 42.7.4)
  - **IDE de Desarrollo:** IntelliJ IDEA

---

## 📂 Arquitectura y Estructura del Proyecto
El proyecto sigue una arquitectura modular en capas para desacoplar la interfaz gráfica de la lógica de acceso a datos:

```text
SegundoCorteV2/
├── .gitignore
├── pom.xml
├── config.example.properties
├── README.md
├── pruebas.md
├── sql/
│   ├── estructura.sql
│   └── datos.sql
├── evidencias/
│   ├── 01-listado-inicial.png
│   ├── 02-filtro-combinado.png
│   ├── 03-validacion-precio.png
│   ├── 04-crud-creado.png
│   ├── 05-crud-actualizado.png
│   └── 06-crud-eliminado.png
└── src/
    └── main/
        ├── java/
        │   └── me/edu/uam/segundocortev2/
        │       ├── DatabaseConnection.java
        │       ├── HelloApplication.java
        │       ├── HelloController.java
        │       ├── Launcher.java
        │       ├── Producto.java
        │       └── ProductoDAO.java
        └── resources/
            └── me/
                └── um/
                    └── segundocortev2/
                        └── hello-view.fxml


```markdown
##  Características Principales y Funcionalidades (CRUD)
El sistema implementa el ciclo completo de operaciones sobre la base de datos de manera segura:
1. **Create (Crear):** Inserción mediante sentencias `PreparedStatement` para evitar inyección SQL y validar la unicidad del código.
2. **Read (Leer):** Consulta general de registros ordenados ascendentemente, reflejados en tiempo real en un `TableView` de 6 columnas.
3. **Update (Actualizar):** Modificación de atributos de un producto existente seleccionado, bloqueando automáticamente el campo ID.
4. **Delete (Eliminar):** Eliminación con diálogo de confirmación previo y actualización inmediata en PostgreSQL y JavaFX.

---

## 🔍 Sistema de Búsquedas, Filtros y Sincronización
- **Búsqueda por texto libre (`ILIKE`):** Coincidencia parcial por código o nombre sin distinción de mayúsculas/minúsculas.
- **Filtro por Categoría:** Selección dinámica mediante ComboBox.
- **Filtro de Existencias:** Casilla para aislar productos con stock activo (`stock > 0`).
- **Contador Dinámico:** Etiqueta inferior que actualiza el número exacto de filas visibles.

---

## 🛡️ Validaciones y Manejo de Excepciones
- **Validaciones de Entrada (V1 - V10):** Verificación previa a la persistencia para evitar cadenas vacías, longitudes inválidas, categorías nulas, precios con comas y stocks negativos.
- **Control de Excepciones (E1 - E4):** Captura de excepciones SQL, violaciones de restricciones de integridad y registro de códigos `SQLState` en consola con alertas visuales.

---

##  Guía de Configuración y Ejecución
1. **Base de Datos:** Cree una base de datos en PostgreSQL llamada `papeleria_campus`. Ejecute secuencialmente los scripts de `sql/` (`estructura.sql` y `datos.sql`).
2. **Credenciales:** Duplique `config.example.properties`, renómbrelo a `config.properties` y configure su URL, usuario y contraseña locales.
3. **Ejecución:** Abra el proyecto en IntelliJ IDEA como proyecto Maven y ejecute la clase `Launcher.java`.

---

## 📊 Matriz de Pruebas y Evidencias
El comportamiento funcional y las capturas de pantalla operativas se encuentran documentadas en [pruebas.md](pruebas.md) y respaldadas en la carpeta [evidencias/](evidencias/).
