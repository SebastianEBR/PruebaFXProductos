package co.edu.uniquindio.fx10.controlador;

import co.edu.uniquindio.fx10.App;
import co.edu.uniquindio.fx10.modelo.Producto;
import co.edu.uniquindio.fx10.repositorio.ProductoRepository;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para el Dashboard principal
 */
public class DashboardController {

    public Button btnListadoProducto;
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

    private ProductoRepository productoRepository;
    private ObservableList<Producto> listaProductos;
    private List<Node> vistaInicial;


    @FXML
    public void initialize() {
        // Guardamos una copia de los nodos iniciales (los botones del dashboard)
        vistaInicial = new ArrayList<>(contenedorPrincipal.getChildren());
    }

    /**
     * Carga los productos en la tabla
     */
    public void cargarProductos() {
        listaProductos = FXCollections.observableArrayList(productoRepository.getProductos());
        tablaProductos.setItems(listaProductos);
    }

    /**
     * Maneja el evento de click en el botón "Crear Producto"
     */
    @FXML
    private void onFormularioProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/vista/FormularioProducto.fxml"));
            Parent formulario = loader.load();
            
            // Obtener el controlador del formulario
            FormularioProductoController controller = loader.getController();
            controller.setDashboardController(this);
            
            // Reemplazar el contenido del contenedor principal
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(formulario);
            
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar el formulario", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de click en el botón "Eliminar"
     */
    @FXML
    private void onListadoProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/vista/TablaProductos.fxml"));
            Parent formulario = loader.load();

            // Obtener el controlador del formulario
            TablaProductosController controller = loader.getController();
            controller.setDashboardController(this);

            // Reemplazar el contenido del contenedor principal
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(formulario);

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la Tabla", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
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
        contenedorPrincipal.getChildren().setAll(vistaInicial);
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

