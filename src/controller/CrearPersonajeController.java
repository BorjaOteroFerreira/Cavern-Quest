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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Personaje;
import model.PersonajeEx;

public class CrearPersonajeController implements Initializable {
    int dadosPersonaje = 3 ; 
    @FXML 
    Label lblAtributos; 
    @FXML 
    Label lblNombre; 
    @FXML
    Button btnCrear;
    @FXML
    Button btnIniciar;
    @FXML
    TextField txtNombre; 
    @FXML
    ComboBox<PersonajeEx.Raza> cmbRaza;
    @FXML
    PersonajeEx p;
    @FXML
    HBox hBoxAtributos;
    @FXML
    ImageView visorImagen;
    @FXML
    Label lblRaza;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font hermona18px = App.cargarFuente("Hermona.ttf", 18);
        lblNombre.setFont(hermona18px);
        lblRaza.setFont(hermona18px);
        lblAtributos.setFont(hermona18px);
        btnCrear.setFont(hermona18px);
        btnIniciar.setFont(hermona18px);
        cmbRaza.getItems().addAll(Personaje.Raza.values());
        //eventos
        btnCrear.setOnAction(e -> crearPersonaje());
        btnIniciar.setOnAction(e -> {
            try {
                iniciarJuego(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        cmbRaza.setOnAction(e -> cargarIcono());
    }
    
    
    void cargarIcono(){
        String raza = String.valueOf(cmbRaza.getValue()).toLowerCase();
        try{
            visorImagen.setImage(new Image("/img/razas/"+raza+"/"+raza+"_sur.png"));
        }catch(Exception e){
            visorImagen.setImage(new Image("/img/razas/humano/humano_sur.png"));
        }
    }

    @FXML
    void iniciarJuego(Event e) throws IOException{
        System.out.println("Comenzar");
        Parent root = FXMLLoader.load(getClass().getResource("/view/Mapa.fxml"));
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setTitle("Cavern Quest - Mapa 1");
        source.getScene().setRoot(root);
        root.setCursor(App.cursor);
        App.scene = source.getScene();
        stage.sizeToScene();
    }
    
    @FXML
    void crearPersonaje(){
        dadosPersonaje--;
        String raza = String.valueOf(cmbRaza.getValue());
        PersonajeEx p = MapaController.p = new PersonajeEx(txtNombre.getText(), raza);
        p.mostrar();
        lblAtributos.setText("Refrescos restantes: "+ dadosPersonaje +"\n\nFuerza: " + p.getFuerza() + 
        "\nAgilidad: " + p.getAgilidad() +  
        "\nConstitucion: "+ p.getConstitucion() + 
        "\nPuntos de vida: " + p.getPuntosVida() +
        "\nExperiencia: " + p.getExperiencia() + 
        "\nNivel: "+ p.getNivel());
        btnIniciar.setDisable(false);
        if (dadosPersonaje < 1)
            btnCrear.setDisable(true);
    }
}






































