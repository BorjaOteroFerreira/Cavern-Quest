package controller;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;
import app.App;
import javafx.scene.text.Font;
import javafx.animation.PauseTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Arma;
import model.Armadura;
import model.Item;
import model.Mapa;
import model.Monstruo;
import model.PersonajeEx;



public  class MapaController implements Initializable, Serializable {
    
    //Constantes
    private final double SIZE_PX_IMG = 64;
    
    //Stage , contenedores y mapas
    Stage stage = App.stage;
    private GridPane gridPane; //contenedor del mapa
    static int numMapa = 0; 
    static int[][] mapa = Mapa.mapas[numMapa]; //mapa actual

    //Variables globales
    private int huir = 3 ;                      //Intentos de huida restantes 
    private boolean pulsacionBloqueada = false; //Bloqueo de pulsaciones
    static int ancho_mapa = mapa[0].length;     //Ancho de mapa en columnas
    static int alto_mapa = mapa.length;         //Alto del mapa en filas
    static PersonajeEx p;                       //Personaje
    static Monstruo npc ;                       //Monstruo 
    private int c ; int f;                      //Posicion personaje 
    
 
    //variables de imagen  
    private static Image imgPersonajeSur;                               //Img personaje Sur
                     
    private static Image imgMuro = new Image("/img/muro01.jpg");    // Muros del mapa
    private static Image imgFuente = new Image("/img/cura01.png");  // Pocion de vida (curacion)
    private static Image imgSuelo = new Image("/img/suelo.png");    // Imagen del suelo                      
    private static Image imgSalida = new Image("/img/salida.png");  // Vortice de salia 
    private static Image imgCofre = new Image("/img/cofre.png");    //cofre


    //Variables de la vista (controles y contenedores)
    @FXML
    public VBox vbox;
    public Label lblFuerza;
    public Label lblAgilidad;
    public Label lblConstitucion;
    public Label lblNivel; 
    public Label lblExperiencia; 
    public Button btnSalir;
    public HBox hbox; 
    public ProgressBar pbarVida;
    public Label lblHp;

    //-- Pantalla de combate --
    public Pane contenedorCombate;
    public ImageView imgCombatePj;
    public ImageView imgCombateNpc;
    public Label logCombatePj; 
    public Label logCombateNpc; 
    public Label lblCombatePj;
    public Label lblCombateNpc;
    public Button btnHuir;
    public Button btnAtacar;
    public Label lblTituloCombate;
    
    // -- Boton reiniciar nivel --
    public Button btnReiniciar;
    
    // -- Pantalla de pausa --
    public Pane contenedorPausa;
    public Label lblPausa; 
    public Button btnPSalir; 
    public Button btnGuardar; 
    public Button btnCargarPartida;
    public Button btnVolverPausa;

    //-- Pantalla inventario 
    public Pane contenedorInventario;
    public ListView<Item> listInventario;
    public Label lblNombre; 
    public Label lblPrecio; 
    public Label lblStat; 
    public Label lblTipo; 
    public Label lblEmpuñadura;
    public ImageView imgImagen;
    public Label lblMonedas;
    public ListView<Item> listEquipo;
    public Label lblTituloInventario;
    public Button btnEquipar; 
    public Button btnDesequipar; 
    public Button btnVolverInventario; 
    public Button btnVender;
    public Label lblEquipo;
    public Label lblInventario;



    

    Item yelmo ;
    Item espada;


    @FXML
    public void volverInventario(){
        contenedorInventario.setVisible(false);
        pulsacionBloqueada = false;
    }

    /*AnimationTimer reloj = new AnimationTimer() {
        @Override
        public void handle(long now){
            lblFuerza.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    };*/
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yelmo = new Armadura("Yelmo de hierro",10,10,10,"YELMO", "/img/cura01.png");
        espada = new Arma("Espada del anciano",10,10,10,false , "/img/cofre.png");
        listInventario.getItems().add(yelmo);
        listInventario.getItems().add(espada);
        //reloj.start(); --> prueba de reloj en tiempo real , pendiente ver como trabajar con cronometro
        String raza = String.valueOf(p.getRaza()).toLowerCase();
        establecerImagenPersonaje(raza);
        btnReiniciar.setVisible(false);
        cargarCombate(false);
        cargarPausa(false);
        cargarInventario(false);
        establecerFuentes();
        dibujarMapa(numMapa);
        actualizarBarraVida();
        actualizarHud();
        sonidoAmbiental();
        listInventario.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> itemSeleccionado(newSelection));

        listEquipo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> itemSeleccionado((Item)newSelection));
        

}
//=======================================[VISTA PRINCIPAL DEL MAPA]==========================================================================
/**
 * La función actualiza la pantalla de visualización frontal (HUD) con el nivel, la fuerza, la
 * agilidad, la constitución, la experiencia y los puntos de salud del jugador.
 */
    public void actualizarHud(){
        lblNivel.setText("Nivel: "+  p.getNivel());
        lblFuerza.setText("Fuerza: " + p.getFuerza());
        lblAgilidad.setText("Agilidad: "+ p.getAgilidad());
        lblConstitucion.setText("Constitucion : "+ p.getConstitucion());
        lblExperiencia.setText("Experiencia: "+ p.getExperiencia());
        lblHp.setText(p.toString());
    }
/**
 * La función pinta una celda específica en un GridPane con un color dado.
 * 
 * @param c El índice de columna de la celda que se va a pintar.
 * @param f El parámetro "f" es un número entero que representa el índice de fila de la celda en un
 * diseño GridPane.
 * @param relleno El parámetro "relleno" es un objeto Paint que representa el color de relleno que se
 * aplicará a una celda específica en un GridPane.
 */
    private void pintarCasilla(int c, int f, Paint relleno) {
        gridPane.getChildren();
        // Busca la casilla entre los hijos del GridPane
        gridPane.getChildren()
        .stream()
        .filter(n -> GridPane.getColumnIndex(n) == c && GridPane.getRowIndex(n) == f)
        .forEach( n -> ((Rectangle)n).setFill(relleno));

        for (Node n : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(n) == c && GridPane.getRowIndex(n) == f) {
               
                break;
            }
        }
    }
/**
 * La función mueve una imagen a una nueva posición en una cuadrícula y tiene la posibilidad de
 * desencadenar un evento de combate.
 * 
 * @param c La columna de la posición inicial desde la que moverse.
 * @param f El parámetro "f" representa el número de fila de la posición inicial de la imagen a mover.
 * @param c1 El índice de columna de la celda de destino donde se moverá la imagen.
 * @param f1 El parámetro "f1" representa el número de fila de la celda de destino donde se moverá la
 * imagen.
 * @param img El parámetro "img" es un objeto de la clase "Imagen" que representa una imagen en JavaFX.
 * Se utiliza para establecer la imagen de una celda específica en un tablero de juego.
 */
    void moverDireccion(int c, int f , int c1 , int f1,Image img){
        pintarCasilla(c,f , new ImagePattern(imgSuelo)); //cambia la imagen actual por la del suelo 
        pintarCasilla(c1,f1 , new ImagePattern(img));   //cambia la casilla de destino por la del personaje
        int random = (int) (Math.random() * 10 + 1);
        if(random < 2) cargarCombate(true);
    }
/**
 * Esta función actualiza la visualización del inventario y, opcionalmente, la muestra en la pantalla.
 * 
 * @param mostrar Un valor booleano que determina si mostrar o no el contenedor de inventario. Si es
 * verdadero, se mostrará el contenedor de inventario y la variable pulsacionBloqueada se establecerá
 * en verdadero, lo que significa que el usuario no puede interactuar con el juego mientras el
 * inventario esté abierto. Si es falso,
 */
    void cargarInventario(Boolean mostrar){
        lblMonedas.setText("Monedas: "+p.getMonedas());
        if(mostrar){    
            contenedorInventario.setVisible(mostrar);
            pulsacionBloqueada = true; 
        }
        contenedorInventario.setVisible(mostrar);       
    }

   /**
    * Esta función maneja el movimiento y los eventos del jugador según el mosaico de destino en un
    * mapa del juego.
    * 
    * @param event Un objeto de evento que representa un evento de pulsación de tecla.
    */
    @FXML
    public void mover(Event event) throws IOException  { 
        String tecla = ((KeyEvent)event).getCode().getChar();
        if(!pulsacionBloqueada){
            System.out.println("Tecla: " + ((KeyEvent)event).getCode());
            sonidoPasos(event);
            switch(tecla){

                case "I": cargarInventario(true) ;break; 
                case "P": cargarPausa(true);break; 
                case "W" :                                                                  //pasa cordenadas actuales y siguientes junto con la imagen a setear 
                    if( f-1 >= 0 && mapa[f-1][c] != 1){
                        moverDireccion(c, f , c, f-1,  p.getImagenNorte()); f--; } break;     //mover norte                                
                case "S" :
                    if(f+1 < alto_mapa  && mapa[f+1][c] != 1) {
                        moverDireccion(c, f, c, f+1, p.getImagenSur());  f++;  } break;      //mover sur
                case "D" : 
                    if( c+1 < ancho_mapa && mapa[f][c+1] != 1 ) {
                        moverDireccion(c, f, c+1, f,  p.getImagenEste()); c++; } break;       //mover este 
                case "A" :
                    if(c-1 >= 0  && mapa[f][c-1] != 1) {
                        moverDireccion(c, f, c-1, f,  p.getImagenOeste()); c--; } break;      //mover oeste
            }
            //Eventos en funcion de la casilla de destino 
            switch(mapa[f][c]){
                    case 9:    
                        numMapa++;                                                          //en caso de llegar a la casilla de salida carga siguiente mapa
                        if (numMapa < Mapa.mapas.length) {                                  // Si hay más mapas por cargar
                            int nivel = numMapa + 1;
                            System.out.println("Comenzar mapa " + nivel);
                            cargarMapa(event);
                        }              
                        else{
                            System.out.println("Has Terminado el juego");
                            mostrarPopupFinDeJuego();  
                        }; break;
                    case 7: 
                        Item item = new Arma("Espada de madera",10,10,10,false);
                        p.addToInventario(item);
                        listInventario.getItems().add(item);
                        mapa[f][c] = 0; break;

                    case 8: 
                            //en caso de caer en casilla de cura   
                            sonidoBeber();     
                            p.curar();
                            actualizarBarraVida();
                            actualizarHud();  
                            mapa[f][c]= 0; 
            } 
        }
    } 
/**
 * Esta función dibuja un mapa en una GUI de Java utilizando imágenes y rectángulos.
 * 
 * @param numMapa El número del mapa a dibujar.
 */
    public  void dibujarMapa(int numMapa){
        mapa = Mapa.mapas[numMapa];
        alto_mapa = mapa.length;
        ancho_mapa = mapa[0].length;
        gridPane = new GridPane();
        hbox.getChildren().add(1, gridPane);
        gridPane.getChildren().clear();

        for(int fila = 0 ; fila < alto_mapa; fila++){
            for(int columna = 0; columna < ancho_mapa; columna++){
                int casilla = mapa[fila][columna];
                Rectangle rec = new Rectangle(SIZE_PX_IMG, SIZE_PX_IMG);

                //Pinta una casilla con la imagen correspondiente
                switch(casilla) { 
                    case 0 : rec.setFill(new ImagePattern(imgSuelo));   break;                              //Casilla vacia, Camino
                    case 1 : rec.setFill(new ImagePattern(imgMuro));    break;                              //Muro
                    case 6 : rec.setFill(new ImagePattern(imgPersonajeSur)); c = columna ; f = fila; break; //Personaje
                    case 7 : rec.setFill(new ImagePattern(imgCofre));   break;                              //Cofre
                    case 8 : rec.setFill(new ImagePattern(imgFuente));  break;                              //Cura 
                    case 9 : rec.setFill(new ImagePattern(imgSalida));  break;                              //Meta 
                }
                gridPane.add(rec,columna,fila);
            }
        }
        double altoMapa = gridPane.getRowCount() * SIZE_PX_IMG;
        vbox.setPrefHeight(altoMapa - btnSalir.getPrefHeight());
    }


/**
 * Esta función carga un nuevo mapa y actualiza el título y el tamaño del escenario en una aplicación
 * Java.
 * 
 * @param event El parámetro event es un objeto de la clase Event, que se utiliza para representar un
 * evento que ocurrió en la aplicación. No se usa en la implementación del método, pero se incluye en
 * la firma del método porque lo requiere el método que llama a este método.
 */
    public void cargarMapa(Event event) throws IOException{
        int nivel = numMapa + 1;
        Node nodo = hbox.getChildren().get(1);                                                     //recupera el nodo (GridPane / mapa) del indice 1 del hbox  
        hbox.getChildren().remove(nodo);                                                                 //elimina el nodo (GridPane / mapa) para pintar el nuevo
        Stage stage = App.stage;                                                                         //recupera el stage principal
        dibujarMapa(numMapa); 
        stage.setTitle("Cavern Quest - Mapa " + nivel);
        stage.sizeToScene();                                                                            //establece el tamaño del stage en el mismo que el de la escena tras pintar el mapa
    }

    //Reinicia el mapa y la posicion del personaje
    @FXML
    void reiniciarMapa(Event event) throws IOException{
        btnReiniciar.setVisible(false);
        btnAtacar.setDisable(false);
        btnHuir.setDisable(false);
        p.curar();
        actualizarBarraVida();
        actualizarHud();
        cargarMapa(event);
        pulsacionBloqueada=false;
    }

//=======================================[VISTA COMBATE]==========================================================================
/**
 * Esta función carga una pantalla de combate y genera un monstruo aleatorio contra el que luchar.
 * 
 * @param mostrarCombate Una variable booleana que determina si mostrar o no la pantalla de combate. Si
 * es cierto, se mostrará la pantalla de combate, y si es falso, se ocultará.
 */
    void cargarCombate(boolean mostrarCombate){
        btnHuir.setText("Huir (" + huir + ")" );
        contenedorCombate.setVisible(mostrarCombate);
        if(mostrarCombate){
            pulsacionBloqueada = true;
            npc = Monstruo.generaMonstruoAleatorio();    
            try{
                imgCombateNpc.setImage(new Image("/img/monstruos/"+ npc.getClass().getSimpleName().toLowerCase() + "01.png"));
            }
            catch(Exception e){
                imgCombateNpc.setImage(new Image("/img/monstruos/" + npc.getClass().getSimpleName().toLowerCase() + "01.jpg"));
            }
            imgCombatePj.setImage(imgPersonajeSur);
            cargarStatsCombate(p, npc);
        }
        boolean mostrar = huir > 0 ? false : true; 
        btnHuir.setDisable(mostrar);
    }
/**
 * La función "huir" permite al jugador intentar huir de una situación de combate.
 * 
 * @param e El parámetro "e" es de tipo Evento y se utiliza como entrada al método "huir".
 */
    public void huir(Event e){
        if(huir > 0 )
            contenedorCombate.setVisible(false);
            pulsacionBloqueada = false; 
            huir--;          
    }



/**
 * La función "cargarPausa" establece la visibilidad de un contenedor de pausa y actualiza un texto de
 * etiqueta en consecuencia.
 * 
 * @param mostrarPausa Una variable booleana que indica si la pantalla de pausa debe mostrarse o no. Si
 * es cierto, se mostrará la pantalla de pausa; de lo contrario, se ocultará.
 */
    void cargarPausa(boolean mostrarPausa){
        contenedorPausa.setVisible(mostrarPausa);
        if(mostrarPausa){
            pulsacionBloqueada = true;
        }
        lblPausa.setText("Partida Pausada");
    }
    


/**
 * Esta función maneja la acción de ataque en un escenario de combate entre un jugador y un personaje
 * que no es jugador, actualizando el registro de combate y deshabilitando el botón de ataque durante
 * las pausas.
 * 
 * @param event El parámetro de evento es un objeto de la clase Event, que representa el evento que
 * activó el método. En este caso, se usa para manejar el evento de clic de botón para el botón
 * "Atacar".
 */
@FXML    
void atacar(Event event) throws IOException, InterruptedException{
        String str;
        int ataque;
        btnAtacar.setDisable(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(1)); //crea pausa  entre actualizacion de ataques
        PauseTransition pausa2 = new PauseTransition(Duration.seconds(2)); //crea una pausa para cuando finaliza el combate
        if(p.estaVivo() && npc.estaVivo()){ //Ataca si estan ambos vivos
            logCombateNpc.setText("");
            ataque = p.atacar(npc);
            str = ataque > 0 ? "Asestas un golpe de "+ ataque + " puntos!" : "El enemigo bloquea el ataque" ;
            logCombatePj.setText(str);
            lblCombateNpc.setText(npc.toString());
        }
        else {
            contenedorCombate.setVisible(false);
            pulsacionBloqueada = false; 
            btnAtacar.setDisable(true);
            btnHuir.setDisable(true);
            btnReiniciar.setVisible(true);
        }
        //Turno de monstruo 
        if(npc.estaVivo() && p.estaVivo()){  //Monstruo devuelve ataque de estar ambos vivos
            ataque = npc.atacar(p); 
            str = ataque > 0 ? "Recibes un golpe de " + ataque + " puntos!" : "Bloqueas el ataque del enemigo";
            final String STRFINAL = str;
            pausa.setOnFinished(ev -> {                             //actualiza los lbl cuando finaliza la pausa
                                    logCombatePj.setText("");
                                    logCombateNpc.setText(STRFINAL);
                                    lblCombatePj.setText(p.toString());
                                    btnAtacar.setDisable(false);
                                });
            pausa.play();
        }
        else {
                btnAtacar.setDisable(false);
                logCombatePj.setText("Has acabado con tu adversario!");
                pausa2.play();
                contenedorCombate.setVisible(false);
                pulsacionBloqueada = false; 
                p.setMonedas(p.getMonedas() + 10);
        }
        actualizarBarraVida();
        actualizarHud();
    }

/**
 * La función reiniciarMapa reinicia el mapa del juego y habilita los botones de ataque y huida
 * mientras deshabilita el botón de reinicio.
 * 
 * @param event El parámetro "event" es un objeto de la clase Event, que representa un evento que
 * ocurrió en la aplicación JavaFX. Por lo general, se usa para manejar las interacciones del usuario
 * con los componentes de la GUI, como los clics en los botones o los movimientos del mouse. En este
 * caso, el método "reiniciarMapa" es
 */

/**
 * La función carga estadísticas de combate para un jugador y un monstruo en un programa Java.
 * 
 * @param p Una variable de tipo "Personaje", que probablemente sea una clase que represente a un
 * personaje de jugador en un juego o simulación.
 * @param m El parámetro "m" es de tipo "Monstruo", lo que sugiere que representa un objeto de la clase
 * "Monstruo".
 */
    public void cargarStatsCombate(PersonajeEx p , Monstruo m){
        logCombatePj.setText("");
        logCombateNpc.setText("");
        lblCombateNpc.setText(npc.toString());
        lblCombatePj.setText(p.toString());
    }

    private void itemSeleccionado(Item item){
        if (item != null)
            if(item instanceof Armadura){
                System.out.println(item);
                lblNombre.setText("Nombre: "+item.getNombre()); 
                lblPrecio.setText("Precio: "+String.valueOf(item.getPrecio())); 
                lblTipo.setText("Tipo: "+((Armadura)item).getTipo().toLowerCase()); 
                lblStat.setText("Constitucion : +"+((Armadura)item).getDefensa());
                lblEmpuñadura.setText(""); 
                imgImagen.setImage(item.getImagen());
            }

            else if(item instanceof Arma){
                System.out.println(item);
                lblNombre.setText("Nombre: "+item.getNombre()); 
                lblPrecio.setText("Precio: "+String.valueOf(item.getPrecio())); 
                lblTipo.setText("Tipo: "+((Arma)item).getClass().getSimpleName()); 
                lblStat.setText("Fuerza : +"+ ((Arma)item).getAtaque()); 
                String dosManos = ((Arma)item).getDosManos() == true? "Dos Manos": "Una mano";
                lblEmpuñadura.setText("Embergadura: "+  dosManos ) ;
                imgImagen.setImage(item.getImagen());
        }
    };
//======================================== [MENU INVENTARIO] ========================================
  /**
   * Esta función equipa un artículo del inventario a la lista visual de equipo del jugador
   * 
   */
    @FXML
    public void equipar(){
        Item itemAux = listInventario.getFocusModel().getFocusedItem();
        if(itemAux instanceof Arma)
         if(p.equipar((Arma)itemAux)){
            listEquipo.getItems().add(itemAux);
            listInventario.getItems().remove(itemAux);
            System.out.println("Item Añadido :" + itemAux.toString() );
            actualizarHud();
            actualizarBarraVida();
         }else{
                System.out.println("no se ha añadido: "+ itemAux.toString());
            }
        else if(itemAux instanceof Armadura){
            if(p.equipar((Armadura)itemAux)){
                listEquipo.getItems().add(itemAux);
                listInventario.getItems().remove(itemAux);

                System.out.println("Item Añadido :" + itemAux.toString());
                actualizarHud();
                actualizarBarraVida();
            }
            else{
                System.out.println("no se ha añadido: "+ itemAux.toString());
            }
        }
    }

/**
 * Esta función de Java elimina un elemento seleccionado de una lista de equipos y lo agrega a una
 * lista de inventario, al mismo tiempo que actualiza varios elementos de HUD.
 */
    public void desequipar(){
        Item itemAux = listEquipo.getFocusModel().getFocusedItem();
        if(itemAux != null){
            listInventario.getItems().add(itemAux);
            listEquipo.getItems().remove(itemAux);
            System.out.println("Item Añadido :" + itemAux.toString() );
            p.desequipar(itemAux);
            actualizarHud();
            actualizarBarraVida();
        }
    }
    @FXML
    public void venderItem(){
        Item itemAux = listInventario.getFocusModel().getFocusedItem();
        if(itemAux != null){
        listInventario.getItems().remove(itemAux);
        p.setMonedas(p.getMonedas() + itemAux.getPrecio());
        lblMonedas.setText("Monedas: "+p.getMonedas());
    }}
/**
 * Esta función actualiza una barra de progreso que representa los puntos de salud actuales de un
 * personaje en función de su constitución y los puntos de salud actuales.
 */
    public void actualizarBarraVida(){
        double totalVida = (p.getConstitucion() + 50 ) * 1.0; 
        double vidaActual = p.getPuntosVida() * 1.0;
        double resultado = ((vidaActual / totalVida) * 100) / 100; 
        pbarVida.setProgress(resultado);
    }
/**
 * La función muestra una ventana emergente con un mensaje de felicitación y una imagen, y luego cierra
 * el escenario.
 */
    public void mostrarPopupFinDeJuego() {
        sonidoAplausos();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin del juego");
        ImageView imageView =new ImageView( new Image("img/espadaCursor.png"));
        imageView.setFitHeight(50); // set the height of the image view
        imageView.setFitWidth(50);
        alert.setGraphic(imageView);
        alert.setHeaderText("¡Has terminado el juego!");
        alert.setContentText("Gracias por jugar! vuelve cuando quieras!\n\n\n Autor: Borja Otero Ferreira");
        alert.showAndWait();
        stage.close();
    }
/**
 * La función guarda el estado actual del juego como un archivo JSON y oculta el menú de pausa.
 * 
 * @param e El parámetro "e" es una instancia de la clase Event, que representa un evento que ocurrió
 * en la aplicación JavaFX. Se pasa al método "guardarPartida" como argumento cuando se llama al
 * método. El propósito de este parámetro es proporcionar información sobre el evento que desencadenó
 */
    @FXML 
    void guardarPartida(Event e ){ 
        try (FileOutputStream archivo = new FileOutputStream("Save.dat")) {
            ObjectOutputStream out = new ObjectOutputStream(archivo);
            out.writeObject(p);
            out.writeInt(numMapa);
            out.writeInt(huir);
            out.close();
            System.out.println("La partida se ha guardado correctamente");
        }catch(EOFException eof){
            //fin archivo 
            System.out.println(eof);
        }
        catch(Exception e1){
            e1.printStackTrace();
        }
    }  
/**
 * Esta función carga un estado de juego guardado, incluido el mapa, el personaje y el HUD, y
 * desbloquea la entrada del jugador.
 * 
 * @param e El parámetro "Evento" "e" es un objeto que representa un evento que ocurrió, como hacer
 * clic en un botón o presionar una tecla. Se utiliza en el método "cargarPartidaGuardada" para manejar
 * el evento que desencadena la carga de una partida guardada.
 * @throws ClassNotFoundException
 */
    @FXML 
    void cargarPartidaGuardada(Event e) throws IOException, ClassNotFoundException{
        try (FileInputStream archivo = new FileInputStream("Save.dat")) {
            ObjectInputStream in = new ObjectInputStream(archivo);
            MapaController.p = (PersonajeEx) in.readObject(); 
            numMapa = in.readInt(); 
            huir = in.readInt();
            in.close();
            System.out.println("La partida se ha cargado correctamente");
        }
        catch(EOFException ex){
            //Fin archivo 
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        actualizarHud(); 
        actualizarBarraVida(); 
        String raza = String.valueOf(p.getRaza());
        establecerImagenPersonaje(raza);
        cargarMapa(e); 
        pulsacionBloqueada = false; 
        contenedorPausa.setVisible(false);
    }
/**
 * La función establece las imágenes de un personaje en función de su raza y dirección.
 * 
 * @param raza "raza" es una variable que representa la raza de un personaje en un juego. Se utiliza
 * para especificar la ruta a los archivos de imagen para las diferentes direcciones del personaje
 * (norte, sur, este, oeste).
 */
    private void establecerImagenPersonaje(String raza){
        imgPersonajeSur = new Image("/img/razas/"+raza+"/"+raza+"_sur.png");
    }
/**
 * La función oculta un contenedor y permite que el usuario ingrese nuevamente.
 */
    @FXML 
    void volverPausa(){
        contenedorPausa.setVisible(false);
        pulsacionBloqueada = false; 
    }
/**
 * La función "salir" cierra la ventana actual.
 * 
 * @param e El parámetro "e" es una instancia de la clase Event, que representa un evento que ocurrió
 * en la aplicación JavaFX. En este caso, se utiliza para manejar el evento de hacer clic en un botón o
 * elemento de menú que activa el método "salir", que cierra la etapa actual (ventana).
 */
@FXML
public void salir(Event e){  //cierra el escenario (ventana)
    stage.close();
}
/**
 * La función reproduce un archivo de sonido de pasos utilizando la clase MediaPlayer de Java.
 * 
 * @param e El parámetro "e" es de tipo Evento, es un objeto de evento que activa la
 * función "sonidoPasos".
 */
    void sonidoPasos(Event e){

        //Media pasos = new Media(new File("src\\sound\\pisada.mp3").toURI().toString());
        Media pasos = new Media(new File("sound\\pisada.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(pasos);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
    }

/**
 * La función reproduce un archivo de sonido de aplausos con volumen reducido.
 */
    private void sonidoAplausos(){

       //Media aplausos = new Media(new File("src\\sound\\aplausos.mp3").toURI().toString());
        Media aplausos = new Media(new File("sound\\aplausos.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(aplausos);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
        
    }
    private void sonidoAmbiental(){

        //Media ambiente = new Media(new File("src\\sound\\fx.mp3").toURI().toString());
        Media ambiente = new Media(new File("sound\\fx.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(ambiente);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }
    private void sonidoBeber(){
        //Media beber = new Media(new File("src\\sound\\poti.mp3").toURI().toString());
        Media beber = new Media(new File("sound\\poti.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(beber);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
    }


/**
 * Esta función establece la fuente de varias etiquetas y botones en una aplicación Java.
 */
public void establecerFuentes(){
    Font hermona28px = App.cargarFuente("Hermona.ttf", 28);
    Font hermona18px = App.cargarFuente("Hermona.ttf",26);
    Font hermona16px = App.cargarFuente("Hermona.ttf",18);
    lblHp.setFont(hermona28px);
    lblFuerza.setFont(hermona18px);
    lblAgilidad.setFont(hermona18px);
    lblConstitucion.setFont(hermona18px);
    lblExperiencia.setFont(hermona18px);
    lblNivel.setFont(hermona18px);
    btnSalir.setFont(hermona18px);
    lblCombateNpc.setFont(hermona18px);
    lblCombatePj.setFont(hermona18px);
    logCombatePj.setFont(hermona16px);
    logCombateNpc.setFont(hermona16px);
    lblTituloCombate.setFont(hermona28px);
    btnCargarPartida.setFont(hermona16px);
    btnGuardar.setFont(hermona16px);
    btnReiniciar.setFont(hermona16px);
    btnPSalir.setFont(hermona16px);
    lblPausa.setFont(hermona18px);
    btnVolverPausa.setFont(hermona16px);
    lblNombre.setFont(hermona16px); 
    lblPrecio.setFont(hermona16px); 
    lblStat.setFont(hermona16px); 
    lblTipo.setFont(hermona16px); 
    lblEmpuñadura.setFont(hermona16px);
    lblTituloInventario.setFont(hermona18px);
    btnEquipar.setFont(hermona16px); 
    btnDesequipar.setFont(hermona16px); 
    btnVolverInventario.setFont(hermona16px);
    lblMonedas.setFont(hermona16px);
    btnVender.setFont(hermona16px);
    lblInventario.setFont(hermona18px);
    lblEquipo.setFont(hermona18px);
    }
}  

    


