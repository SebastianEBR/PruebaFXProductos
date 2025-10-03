package co.edu.uniquindio.fx10.controlador;

import co.edu.uniquindio.fx10.App;
import co.edu.uniquindio.fx10.modelo.Producto;
import co.edu.uniquindio.fx10.repositorio.ProductoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Controlador para el Dashboard principal
 */
public class TablaProductosController {

    @FXML
    private Button btnBack;

    @FXML
    private VBox productsTable;

    @FXML
    private VBox contenedorPrincipal;

    @FXML
    private Label lblTitulo;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colDescripcion;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private Button btnFormularioProducto;

    @FXML
    private VBox btnTablaProducto;

    @FXML
    private Button btnEliminar;

    private DashboardController dashboardController;
    private ProductoRepository productoRepository;
    private ObservableList<Producto> listaProductos;

    @FXML
    public void initialize() {
        productoRepository = ProductoRepository.getInstancia();

        // Configurar las columnas de la tabla
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Formatear la columna de precio
        colPrecio.setCellFactory(column -> new TableCell<Producto, Double>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", precio));
                }
            }
        });

        // Cargar los productos
        cargarProductos();
    }

    /**
     * Carga los productos en la tabla
     */
    public void cargarProductos() {
        listaProductos = FXCollections.observableArrayList(productoRepository.getProductos());
        tablaProductos.setItems(listaProductos);
    }

    /**
     * Establece el controlador del dashboard para poder regresar
     */
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
        this.contenedorPrincipal = dashboardController.getContenedorPrincipal();
    }

    /**
     * Maneja el evento de click en el botón "Volver"
     */
    @FXML
    private void onBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/vista/Dashboard.fxml"));
        Parent dashboard = loader.load();
        DashboardController ctr = loader.getController();

        contenedorPrincipal.getChildren().clear(); // 👈 Limpia antes
        contenedorPrincipal.getChildren().add(dashboard);
    }

    /**
     * Maneja el evento de click en el botón "Eliminar"
     */
    @FXML
    private void onEliminarProducto() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor seleccione un producto para eliminar", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar el producto?");
        confirmacion.setContentText("Producto: " + productoSeleccionado.getNombre());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                productoRepository.eliminarProducto(productoSeleccionado);
                cargarProductos();
                mostrarAlerta("Éxito", "Producto eliminado correctamente", Alert.AlertType.INFORMATION);
            }
        });
    }

    /**
     * Restaura la vista del dashboard
     */
    public void restaurarVista() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/vista/Dashboard.fxml"));
            Parent dashboard = loader.load();

            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(dashboardController.getContenedorPrincipal());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra una alerta al usuario
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public VBox getContenedorPrincipal() {
        return contenedorPrincipal;
    }
}