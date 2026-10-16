package me.edu.uam.segundocortev2;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import me.edu.uam.segundocortev2.dao.ProductoDAO;
import me.edu.uam.segundocortev2.modelo.Producto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HelloController {

    private static final Set<String> CATEGORIAS_VALIDAS = Set.of("Papelería", "Tecnología", "Accesorios");
    private static final BigDecimal PRECIO_MIN = new BigDecimal("0.01");
    private static final BigDecimal PRECIO_MAX = new BigDecimal("99999999.99");

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroCategoria;
    @FXML private CheckBox chkSoloExistencias;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private Label lblContador;
    @FXML private Label lblEstado;

    @FXML private TextField txtId;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private Integer idProductoSeleccionado = null;

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarCombos();
        configurarSeleccionTabla();
        cargarDatosDesdeBD(false);
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getIdProducto()).asObject());
        colCodigo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCodigo()));
        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colCategoria.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoria()));
        colPrecio.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPrecio()));
        colStock.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getStock()).asObject());

        colPrecio.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });

        tablaProductos.setItems(listaProductos);
    }

    private void configurarCombos() {
        cmbFiltroCategoria.setItems(FXCollections.observableArrayList("Todas", "Papelería", "Tecnología", "Accesorios"));
        cmbFiltroCategoria.setValue("Todas");
        cmbCategoria.setItems(FXCollections.observableArrayList("Papelería", "Tecnología", "Accesorios"));
    }

    private void configurarSeleccionTabla() {
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, seleccionado) -> {
            if (seleccionado != null) {
                idProductoSeleccionado = seleccionado.getIdProducto();
                txtId.setText(String.valueOf(seleccionado.getIdProducto()));
                txtCodigo.setText(seleccionado.getCodigo());
                txtNombre.setText(seleccionado.getNombre());
                cmbCategoria.setValue(seleccionado.getCategoria());
                txtPrecio.setText(seleccionado.getPrecio().toPlainString());
                txtStock.setText(String.valueOf(seleccionado.getStock()));
                lblEstado.setText("Producto seleccionado: " + seleccionado.getCodigo() + " (ID: " + idProductoSeleccionado + ")");
            }
        });
    }

    @FXML
    private void onBuscarClick() {
        cargarDatosDesdeBD(true);
    }

    @FXML
    private void onLimpiarFiltrosClick() {
        txtBuscar.clear();
        cmbFiltroCategoria.setValue("Todas");
        chkSoloExistencias.setSelected(false);
        cargarDatosDesdeBD(false);
    }

    @FXML
    private void onRecargarClick() {
        cargarDatosDesdeBD(false);
    }

    private void cargarDatosDesdeBD(boolean mostrarAlertaSinCoincidencias) {
        String texto = txtBuscar.getText() != null ? txtBuscar.getText() : "";
        String categoria = cmbFiltroCategoria.getValue() != null ? cmbFiltroCategoria.getValue() : "Todas";
        boolean soloExistencias = chkSoloExistencias.isSelected();

        try {
            List<Producto> resultados = productoDAO.buscarConFiltros(texto, categoria, soloExistencias);
            listaProductos.setAll(resultados);
            lblContador.setText("Registros visibles: " + listaProductos.size());

            if (listaProductos.isEmpty()) {
                lblEstado.setText("Sin coincidencias.");
                if (mostrarAlertaSinCoincidencias) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sin coincidencias",
                            "Sin coincidencias: no se encontraron productos que cumplan con los filtros indicados.");
                }
            } else {
                lblEstado.setText("Consulta completada (" + listaProductos.size() + " productos).");
            }
        } catch (SQLException e) {
            lblEstado.setText("Error de conexión / consulta a la base de datos.");
            lblContador.setText("Registros visibles: -- (Error de conexión)");
            mostrarErrorSQL("No se pudo consultar el catálogo en PostgreSQL.", e);
        }
    }

    @FXML
    private void onNuevoLimpiarClick() {
        limpiarFormulario();
        lblEstado.setText("Formulario limpio para nuevo registro.");
    }

    private void limpiarFormulario() {
        idProductoSeleccionado = null;
        tablaProductos.getSelectionModel().clearSelection();
        txtId.clear();
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.setValue(null);
        txtPrecio.clear();
        txtStock.clear();
    }

    @FXML
    private void onGuardarClick() {
        try {
            Producto nuevo = validarYConstruirProducto(null);
            if (nuevo == null) {
                return;
            }

            int filas = productoDAO.insertar(nuevo);
            if (filas > 0) {
                limpiarFormulario();
                cargarDatosDesdeBD(false);
                lblEstado.setText("Producto '" + nuevo.getCodigo() + "' guardado correctamente.");
                mostrarAlerta(Alert.AlertType.INFORMATION, "Guardado exitoso",
                        "El producto con código " + nuevo.getCodigo() + " se registró correctamente.");
            }
        } catch (SQLException e) {
            lblEstado.setText("Error al guardar el producto.");
            mostrarErrorSQL("No se pudo guardar el producto en la base de datos.", e);
        }
    }

    @FXML
    private void onActualizarClick() {
        if (idProductoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Debe seleccionar un producto de la tabla antes de actualizar.");
            return;
        }

        try {
            Producto editado = validarYConstruirProducto(idProductoSeleccionado);
            if (editado == null) {
                return;
            }
            editado.setIdProducto(idProductoSeleccionado);

            int filas = productoDAO.actualizar(editado);
            if (filas == 0) {
                lblEstado.setText("El producto (ID " + idProductoSeleccionado + ") ya no existe.");
                mostrarAlerta(Alert.AlertType.WARNING, "Producto no encontrado",
                        "El producto seleccionado (ID " + idProductoSeleccionado + ") ya no existe en la base de datos.\n" +
                                "Se recargará el listado actual.");
                limpiarFormulario();
                cargarDatosDesdeBD(false);
                return;
            }

            int idActualizado = idProductoSeleccionado;
            limpiarFormulario();
            cargarDatosDesdeBD(false);
            lblEstado.setText("Producto ID " + idActualizado + " actualizado correctamente.");
            mostrarAlerta(Alert.AlertType.INFORMATION, "Actualización exitosa",
                    "El producto '" + editado.getCodigo() + "' se actualizó correctamente.");

        } catch (SQLException e) {
            lblEstado.setText("Error al actualizar el producto.");
            mostrarErrorSQL("No se pudo actualizar el producto en la base de datos.", e);
        }
    }

    @FXML
    private void onEliminarClick() {
        if (idProductoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Debe seleccionar un producto de la tabla antes de eliminar.");
            return;
        }

        String codigoMostrar = txtCodigo.getText() != null ? txtCodigo.getText().trim() : "";

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Desea eliminar el producto seleccionado?");
        confirmacion.setContentText("ID: " + idProductoSeleccionado + " | Código: " + codigoMostrar +
                "\nEsta acción eliminará el registro de PostgreSQL.");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
            lblEstado.setText("Eliminación cancelada por el usuario.");
            return;
        }

        try {
            int filas = productoDAO.eliminar(idProductoSeleccionado);
            if (filas == 0) {
                lblEstado.setText("El producto (ID " + idProductoSeleccionado + ") ya no existía.");
                mostrarAlerta(Alert.AlertType.WARNING, "Producto no encontrado",
                        "El producto seleccionado ya no existe en la base de datos.\n" +
                                "Se recargará el listado actual.");
                limpiarFormulario();
                cargarDatosDesdeBD(false);
                return;
            }

            limpiarFormulario();
            cargarDatosDesdeBD(false);
            lblEstado.setText("Producto eliminado correctamente.");
            mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminación exitosa",
                    "El producto fue eliminado correctamente de la base de datos.");

        } catch (SQLException e) {
            lblEstado.setText("Error al eliminar el producto.");
            mostrarErrorSQL("No se pudo eliminar el producto de la base de datos.", e);
        }
    }

    private Producto validarYConstruirProducto(Integer idExcluir) throws SQLException {
        String codigoRaw = txtCodigo.getText() != null ? txtCodigo.getText().trim().toUpperCase() : "";
        String nombreRaw = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        String categoriaRaw = cmbCategoria.getValue() != null ? cmbCategoria.getValue().trim() : "";
        String precioRaw = txtPrecio.getText() != null ? txtPrecio.getText().trim() : "";
        String stockRaw = txtStock.getText() != null ? txtStock.getText().trim() : "";

        txtCodigo.setText(codigoRaw);
        txtNombre.setText(nombreRaw);

        if (codigoRaw.isEmpty() || codigoRaw.length() > 10) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Código (V1)",
                    "El código es obligatorio y debe tener entre 1 y 10 caracteres (sin espacios laterales).");
            return null;
        }

        if (nombreRaw.length() < 3 || nombreRaw.length() > 80) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Nombre (V2)",
                    "El nombre es obligatorio y debe tener entre 3 y 80 caracteres tras quitar espacios laterales.");
            return null;
        }

        if (categoriaRaw.isEmpty() || !CATEGORIAS_VALIDAS.contains(categoriaRaw)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Categoría (V3)",
                    "Debe seleccionar una categoría válida: Papelería, Tecnología o Accesorios.");
            return null;
        }

        if (precioRaw.isEmpty() || precioRaw.contains(",")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V4)",
                    "Ingrese un precio numérico válido utilizando punto decimal (ejemplo: 65.00).");
            return null;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(precioRaw);
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V4)",
                    "El precio ingresado no es un número válido.");
            return null;
        }

        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V5)",
                    "El precio debe ser mayor que 0 (mínimo 0.01).");
            return null;
        }

        if (precio.scale() > 2) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V6)",
                    "El precio no puede tener más de 2 decimales.");
            return null;
        }
        if (precio.compareTo(PRECIO_MIN) < 0 || precio.compareTo(PRECIO_MAX) > 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V6)",
                    "El precio está fuera del rango permitido (0.01 a 99999999.99).");
            return null;
        }

        if (stockRaw.isEmpty() || !stockRaw.matches("^-?\\d+$")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Stock (V7)",
                    "El stock debe ser un número entero válido (sin decimales ni letras).");
            return null;
        }

        long stockLong;
        try {
            stockLong = Long.parseLong(stockRaw);
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Stock (V8)",
                    "El stock está fuera del rango entero permitido (0 a 2147483647).");
            return null;
        }

        if (stockLong < 0 || stockLong > Integer.MAX_VALUE) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Stock (V8)",
                    "El stock debe ser un número entero entre 0 y 2147483647.");
            return null;
        }
        int stock = (int) stockLong;

        if (productoDAO.existeCodigo(codigoRaw, idExcluir)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado (V9)",
                    "Ya existe otro producto registrado con el código '" + codigoRaw + "'.");
            return null;
        }

        return new Producto(codigoRaw, nombreRaw, categoriaRaw, precio, stock);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarErrorSQL(String contexto, SQLException e) {
        String sqlState = e.getSQLState() != null ? e.getSQLState() : "Desconocido";
        String mensajeUsuario;

        if (sqlState.startsWith("08")) {
            mensajeUsuario = "No se pudo establecer conexión con el servidor PostgreSQL.\n" +
                    "Verifique que el servicio esté activo y que el puerto/configuración en 'config.properties' sean correctos.";
        } else if ("23505".equals(sqlState)) {
            mensajeUsuario = "Restricción de unicidad: ya existe un producto con ese código.";
        } else if ("23514".equals(sqlState)) {
            mensajeUsuario = "Restricción de integridad (CHECK): uno de los datos incumple las reglas de la tabla.";
        } else if (sqlState.startsWith("28")) {
            mensajeUsuario = "Error de autenticación con PostgreSQL. Revise las credenciales en 'config.properties'.";
        } else {
            mensajeUsuario = "Ocurrió un error al procesar la operación en la base de datos.";
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Base de Datos");
        alert.setHeaderText(contexto);
        alert.setContentText(mensajeUsuario + "\n(Código de estado SQL: " + sqlState + ")");
        alert.showAndWait();
    }
}