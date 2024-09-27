package app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application{
    
    public static Stage stage;
    public static Scene scene;
    static Image cursorImage = new Image("/img/espadaCursor.png");
    // Crear un objeto ImageCursor con la imagen personalizada
    public static Cursor cursor = new ImageCursor(cursorImage);

    public static void main(String[] args) throws Exception {
        System.out.println("Iniciado");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage; // Guarda la referencia a la ventana principal en una variable estática global.
        primaryStage.setTitle("Cavern Quest");
        Parent root = FXMLLoader.load(getClass().getResource("../view/Inicio.fxml"));
        primaryStage.getIcons().add(new Image("/img/icon.png"));
        primaryStage.setScene(new Scene(root));
        root.setCursor(cursor);
        primaryStage.show();
    }
    public static void cargarEscena(String titulo, Scene root){
        stage.setTitle(titulo);
        stage.setScene(root);   
      
    }
    public static Font cargarFuente(String fuente , int size){
        return  Font.loadFont(App.class.getResourceAsStream("/fonts/"+fuente), size);
    }
    

}