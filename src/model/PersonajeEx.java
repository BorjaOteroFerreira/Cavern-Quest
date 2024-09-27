package model;

import java.io.Serializable;
/**
 * Extiende la clase Personaje y agrega algunos atributos y métodos más.
 */
import java.util.Arrays;

import javafx.scene.image.Image;


public class PersonajeEx extends Personaje implements Serializable{

    int monedas;
    Item[] inventario;
    Item itemManoIzq;
    Item itemManoDch;
    Armadura armaduraCabeza;
    Armadura armaduraCuerpo;
    

// un constructor
    public PersonajeEx(String nombre, String raza, int constitucion) {
        super(nombre, raza);
        this.constitucion = constitucion; 
        inventario = new Item[0];
    }

    public PersonajeEx(String nombre, String raza) {
        super(nombre, raza);
        inventario = new Item[0];
    }

    public Image getImagenNorte() {
        return new Image(imagenNorte);
    }

    public Image getImagenSur() {
        return new Image(imagenSur);
    }

    public Image getImagenEste() {
        return new Image(imagenEste);
    }

    public Image getImagenOeste() {
        return new Image(imagenOeste);
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    public Item[] getInventario() {
        return inventario;
    }

    public void setInventario(Item[] inventario) {
        this.inventario = inventario;
    }

/**
 * Si el peso del artículo que se agregará al inventario más el peso actual del inventario es menor o
 * igual al peso máximo del inventario, agregue el artículo al inventario y devuelva verdadero. De lo
 * contrario, imprime un mensaje y devuelve falso.
 * 
 * @param item Artículo
 * @return El método devuelve un valor booleano.
 */
    public boolean addToInventario(Item item) {
        boolean anhadido = false;
        if (getCargaActual() + item.peso <= getCargaMaxima()){
            inventario = Arrays.copyOf(inventario,inventario.length + 1);
            inventario[inventario.length - 1] = item;
            anhadido = true;
        }
        else{
            System.out.println("No tienes espacio suficiente en el inventario");
        }            
        return anhadido;
    }

/**
 * Esta función imprime el inventario del jugador.
 */
    public void mostrarInventario() {
        System.out.println("Inventario de " + nombre + ":");
        for(int i = 1; i <= inventario.length; i++)
            System.out.print(i + ". " + inventario[i - 1]);
    }


/**
 * La función comprar() toma un Item como parámetro y devuelve un booleano. Si el artículo no es nulo y
 * el precio del artículo es menor o igual que el dinero del jugador, el artículo se agrega al
 * inventario del jugador y la función devuelve verdadero. De lo contrario, la función devuelve falso.
 * 
 * @param item el artículo a comprar
 */
    public boolean comprar(Item item){
        boolean res = false; 
        if(item != null){
            if(item.precio <= getMonedas()){
                addToInventario(item);
                System.out.println("Gracias por comprar" + item.nombre);
                res = true;

            }else{ 
                System.out.println("No tienes dinero suficiente para comprar" + item.nombre);
                res = false;
            }
        
        }else
            res = false;
        
        return res;
    }

    public Item getItemManoIzq() {
        return itemManoIzq;
    }

    public void setItemManoIzq(Item itemManoIzq) {
        this.itemManoIzq = itemManoIzq;
    }

    public Item getItemManoDch() {
        return itemManoDch;
    }

    public void setItemManoDch(Item itemManoDch) {
        this.itemManoDch = itemManoDch;
    }

    public double getCargaActual() {
        double peso = 0;
        for (Item i: inventario)
            peso += i.peso;
        return peso;
    }

    public int getCargaMaxima() {
        return 50 + constitucion * 2;
    }

/**
 * Si el artículo es un casco, equípalo en la cabeza. Si es un escudo, equípalo en la mano derecha. Si
 * es un chaleco antibalas, equípalo en el cuerpo.
 * 
 * @param armadura es la armadura que se va a equipar
 * @return El método devuelve un valor booleano.
 */
    public boolean equipar(Armadura armadura) {
        boolean equipado = false;
        switch (armadura.tipo){
            case YELMO: if (armaduraCabeza == null) {
                            armaduraCabeza = armadura;
                            equipado = true;
                        }
                        break;
            case ARMADURA: if (armaduraCuerpo == null) {
                armaduraCuerpo = armadura;
                equipado = true;
            }
            break;
            case ESCUDO: 
            if (itemManoDch == null) {
                itemManoDch = armadura;
                equipado = true;
            } else if (itemManoIzq == null) {
                itemManoIzq = armadura;
                equipado = true;
            }
            break;
            
        }
        if (equipado)
            constitucion += armadura.getDefensa();
        return equipado;
    }

/**
 * Si el arma es a dos manos y el jugador no tiene armas equipadas, equipa el arma con ambas manos. De
 * lo contrario, si el arma es de una mano y el jugador no tiene un arma equipada en la mano derecha,
 * equipe el arma en la mano derecha. De lo contrario, si el jugador no tiene un arma equipada en la
 * mano izquierda, equipar el arma en la mano izquierda
 * 
 * @param arma el arma a equipar
 * @return El método devuelve un valor booleano.
 */
    public boolean equipar(Arma arma) {
        boolean equipado = false;
        if (arma.dosManos) {
            if (itemManoDch == null && itemManoIzq == null){
                itemManoDch = arma;
                itemManoIzq = arma;
                equipado = true;
            }
        } else {
            if (itemManoDch == null) {
                itemManoDch = arma;
                equipado = true;
            } else if (itemManoIzq == null) {
                itemManoIzq = arma;
                equipado = true;
            }
        }
        mostrarEquipo();
        if (equipado)
            fuerza += arma.getAtaque();
        
        return equipado;                
    }

public Armadura getArmaduraCabeza() {
    return armaduraCabeza;
}

public Armadura getArmaduraCuerpo() {
    return armaduraCuerpo;
}

    //TODO : refactorizar
    public void desequipar(Item item){
        if(item != null){
            if(itemManoDch != null && itemManoDch.equals(item)){
                itemManoDch = null;
                fuerza -= ((Arma)item).getAtaque();
                
            }
            if (itemManoIzq != null && itemManoIzq.equals(item)){
                itemManoIzq = null;
                if(!((Arma)item).getDosManos())
                    fuerza -= ((Arma)item).getAtaque();
            }
            if(armaduraCabeza != null && armaduraCabeza.equals(item)){
                armaduraCabeza = null;
                constitucion -= ((Armadura)item).getDefensa();
                
            }
            if (armaduraCuerpo != null && armaduraCuerpo.equals(item)){
                armaduraCuerpo = null;
                constitucion -= ((Armadura)item).getDefensa();
            }
        }
    }
/**
 * Imprime el nombre del personaje, luego imprime el nombre de los elementos que lleva puestos el
 * personaje.
 */
    public void mostrarEquipo() {
        System.out.println("Equipo de combate de " + nombre + ":");
        System.out.println("- " + (armaduraCabeza != null ? armaduraCabeza:"Yelmo no equipado"));
        System.out.println("- " + (armaduraCuerpo != null ? armaduraCuerpo:"Sin armadura"));
        if (itemManoDch == itemManoIzq)
            System.out.println("- " + (itemManoDch != null ? itemManoDch:"Manos vacías"));
        else {
            System.out.println("- " + (itemManoDch != null ? itemManoDch:"Mano derecha vacía"));
            System.out.println("- " + (itemManoIzq != null ? itemManoIzq:"Mano izquierda vacía"));
        }
        System.out.println();

    }
}