package me.edu.uam.segundocorte;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HelloController {

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroCategoria;
    @FXML private CheckBox chkSoloExistencias;
    @FXML private Label lblContador;
    @FXML private Label lblEstado;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private TextField txtId;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private Integer idSeleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tablaProductos.setItems(listaProductos);

        cmbFiltroCategoria.setItems(FXCollections.observableArrayList("Todas", "Papelería", "Tecnología", "Accesorios"));
        cmbFiltroCategoria.setValue("Todas");

        cmbCategoria.setItems(FXCollections.observableArrayList("Papelería", "Tecnología", "Accesorios"));

        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            if (actual != null) {
                cargarProductoEnFormulario(actual);
            }
        });

        ejecutarConsulta(false);
    }

    private void cargarProductoEnFormulario(Producto p) {
        idSeleccionado = p.getIdProducto();
        txtId.setText(String.valueOf(p.getIdProducto()));
        txtCodigo.setText(p.getCodigo());
        txtNombre.setText(p.getNombre());
        cmbCategoria.setValue(p.getCategoria());
        txtPrecio.setText(p.getPrecio().toPlainString());
        txtStock.setText(String.valueOf(p.getStock()));
    }

    @FXML
    private void onBuscarClick() {
        ejecutarConsulta(true);
    }

    @FXML
    private void onLimpiarFiltrosClick() {
        txtBuscar.clear();
        cmbFiltroCategoria.setValue("Todas");
        chkSoloExistencias.setSelected(false);
        ejecutarConsulta(false);
    }

    @FXML
    private void onRecargarClick() {
        ejecutarConsulta(false);
    }

    @FXML
    private void onNuevoLimpiarFormularioClick() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtId.clear();
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.setValue(null);
        txtPrecio.clear();
        txtStock.clear();
        tablaProductos.getSelectionModel().clearSelection();
    }

    private void ejecutarConsulta(boolean mostrarAvisoSinCoincidencias) {
        try {
            String texto = txtBuscar.getText();
            String categoria = cmbFiltroCategoria.getValue();
            boolean soloStock = chkSoloExistencias.isSelected();

            List<Producto> resultados = productoDAO.buscarProductos(texto, categoria, soloStock);
            listaProductos.setAll(resultados);
            lblContador.setText("Registros visibles: " + listaProductos.size());

            if (resultados.isEmpty()) {
                lblEstado.setText("Sin coincidencias");
                tablaProductos.setPlaceholder(new Label("Sin coincidencias"));
                if (mostrarAvisoSinCoincidencias) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Búsqueda sin resultados", "Sin coincidencias para los criterios ingresados.");
                }
            } else {
                lblEstado.setText("Consulta completada correctamente.");
            }
        } catch (SQLException e) {
            lblEstado.setText("Error de conexión o consulta con la base de datos.");
            mostrarErrorSQL("No fue posible consultar el catálogo en PostgreSQL.", e);
        }
    }

    @FXML
    private void onGuardarClick() {
        try {
            Producto nuevo = ProductoValidador.validarYConstruir(
                    null,
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    cmbCategoria.getValue(),
                    txtPrecio.getText(),
                    txtStock.getText()
            );

            if (productoDAO.existeCodigoEnOtroProducto(nuevo.getCodigo(), null)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado",
                        "Ya existe un producto registrado con el código '" + nuevo.getCodigo() + "'.");
                return;
            }

            int filas = productoDAO.insertar(nuevo);
            if (filas > 0) {
                limpiarFormulario();
                ejecutarConsulta(false);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Producto guardado",
                        "El producto '" + nuevo.getCodigo() + "' se guardó correctamente.");
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", e.getMessage());
        } catch (SQLException e) {
            mostrarErrorSQL("No se pudo guardar el producto.", e);
        }
    }

    @FXML
    private void onActualizarClick() {
        if (idSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Debe seleccionar un producto de la tabla antes de actualizar.");
            return;
        }

        try {
            Producto editado = ProductoValidador.validarYConstruir(
                    idSeleccionado,
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    cmbCategoria.getValue(),
                    txtPrecio.getText(),
                    txtStock.getText()
            );

            if (productoDAO.existeCodigoEnOtroProducto(editado.getCodigo(), idSeleccionado)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado",
                        "El código '" + editado.getCodigo() + "' ya pertenece a otro producto.");
                return;
            }

            int filas = productoDAO.actualizar(editado);
            if (filas == 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Registro no encontrado",
                        "El producto seleccionado ya no existe en la base de datos. Se recargará el listado.");
                limpiarFormulario();
                ejecutarConsulta(false);
                return;
            }

            limpiarFormulario();
            ejecutarConsulta(false);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto actualizado",
                    "El producto se actualizó correctamente.");

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos", e.getMessage());
        } catch (SQLException e) {
            mostrarErrorSQL("No se pudo actualizar el producto.", e);
        }
    }

    @FXML
    private void onEliminarClick() {
        if (idSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Debe seleccionar un producto de la tabla antes de eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar el producto con ID " + idSeleccionado + " (" + txtCodigo.getText() + ")?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
            lblEstado.setText("Eliminación cancelada por el usuario.");
            return;
        }

        try {
            int filas = productoDAO.eliminar(idSeleccionado);
            if (filas == 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Registro no encontrado",
                        "El producto seleccionado ya no existe en la base de datos. Se recargará el listado.");
                limpiarFormulario();
                ejecutarConsulta(false);
                return;
            }

            limpiarFormulario();
            ejecutarConsulta(false);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto eliminado",
                    "El producto fue eliminado correctamente.");
        } catch (SQLException e) {
            mostrarErrorSQL("No se pudo eliminar el producto.", e);
        }
    }

    private void mostrarErrorSQL(String contexto, SQLException e) {
        String estado = e.getSQLState();
        String detalleAmigable;

        if ("23505".equals(estado)) {
            detalleAmigable = "El código ingresado ya existe en la base de datos (restricción UNIQUE).";
        } else if ("23514".equals(estado)) {
            detalleAmigable = "Los datos incumplen una restricción de integridad (CHECK) de la base de datos.";
        } else if (estado != null && estado.startsWith("08")) {
            detalleAmigable = "No se pudo establecer conexión con el servidor PostgreSQL. Verifique que el servicio esté activo y el puerto sea correcto.";
        } else {
            detalleAmigable = "Ocurrió un error al procesar la operación en la base de datos (SQLState: " + estado + ").";
        }

        mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", contexto + "\n" + detalleAmigable);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}