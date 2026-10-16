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
