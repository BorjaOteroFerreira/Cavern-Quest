package model;

import java.io.Serializable;

import javafx.scene.image.Image;

public class Arma extends Item implements Serializable {
    
    int ataque;
    boolean dosManos;


    public Arma(String nombre, double peso, int ataque) {
        super(nombre, peso);
        this.ataque = ataque;
    }


    public Arma(String nombre, double peso, int precio, int ataque, boolean dosManos) {
        super(nombre, peso, precio);
        this.ataque = ataque;
        this.dosManos = dosManos;
    }
    public Arma(String nombre, double peso, int precio, int ataque, boolean dosManos, String imagen ) {
        super(nombre, peso, precio, imagen);
        this.ataque = ataque;
        this.dosManos = dosManos;
    }


    public int getAtaque() {
        return ataque;
    }


    public boolean getDosManos() {
        return dosManos;
    }


    @Override
    public String toString() {
        return nombre + " (+" + ataque + " ataque)";
    }

}
