package model;

import java.io.Serializable;

import javafx.scene.image.Image;

/**
 * Item
 */
public class Item implements Serializable{
    String imagenUrl = "/img/cura01.png";
    String nombre;
    double peso;
    int precio;
    
    public Item() {
    }


    public Item(String nombre, double peso, int precio, String imagen) {
        this.nombre = nombre;
        this.peso = peso;
        this.precio = precio;
        this.imagenUrl = imagen;
    }

    public Item(String nombre, double peso, int precio) {
        this.nombre = nombre;
        this.peso = peso;
        this.precio = precio;
    }

    public Item(String nombre, double peso) {
        this.nombre = nombre;
        this.peso = peso;
    }

    public static Item generarItemAleatorio(){
        return null;
    }

       /*  if (rnd <= 30) 
            // Orco
            i = new Arma();
        else
            // Dragon
            i = new Armadura();

        return i;
    }*/


    /* 
    @Override
    public boolean equals(Object obj) {
        Item i = (Item) obj;
        return nombre == i.nombre && peso == i.peso && precio == i.precio;
    }*/


    public String getNombre() {
        return nombre;
    }

    public Image getImagen(){
        return new Image(imagenUrl);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }


    @Override
    public String toString() {
        return nombre + ", peso: " + peso + " kilos, precio: " + precio + " monedas\n";
    }

    

}