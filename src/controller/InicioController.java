package controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import app.App;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class InicioController implements Initializable  {
    @FXML
    VBox vBox;
    @FXML
    Button btnComenzar;
    @FXML
    Label lblPrincipal;
    @FXML
    ImageView viewerFondo;
    @FXML
    StackPane stackPanel;
    
    @FXML
    public void comenzar(Event e) throws IOException{
        System.out.println("Comenzar");
        Parent root = FXMLLoader.load(getClass().getResource("/view/CrearPersonaje.fxml"));
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setTitle("Cavern Quest - Crear Personaje");
        source.getScene().setRoot(root);
        root.setCursor(App.cursor);
        stage.sizeToScene();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font Hermona = App.cargarFuente("Hermona.ttf",20);
        lblPrincipal.setFont(Hermona);
        btnComenzar.setFont(Hermona);
    }
    

  
}