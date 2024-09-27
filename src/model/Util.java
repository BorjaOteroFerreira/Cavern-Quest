package model;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Util {

    static public void guardarPartida(Personaje p, int c, int f, int numMapa , String nombre){
        try (FileOutputStream archivo = new FileOutputStream("Save.dat")) {
            ObjectOutputStream out = new ObjectOutputStream(archivo);
            out.writeObject(p);
            out.writeInt(c);
            out.writeInt(f);
            out.writeInt(numMapa);
            out.close();
        }catch(EOFException e){
            //Fin archivo 
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    static public void cargarPartida(){}


    
 }
