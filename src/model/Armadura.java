package model;

public class Armadura extends Item {

    int defensa;
    enum TipoArmadura {YELMO, ARMADURA, ESCUDO}
    TipoArmadura tipo;
    
    public Armadura(String nombre, double peso, int precio, int defensa, String tipo) {
        super(nombre, peso, precio);
        this.defensa = defensa;
        this.tipo = TipoArmadura.valueOf(tipo);
    }
    public Armadura(String nombre, double peso, int precio, int defensa, String tipo, String imagen) {
        super(nombre, peso, precio, imagen);
        this.defensa = defensa;
        this.tipo = TipoArmadura.valueOf(tipo);
    }

    public int getDefensa(){
        return defensa;
    }
    
    public String getTipo(){
        return String.valueOf(tipo);
    }

    @Override
    public String toString() {
        return nombre + " (+" + defensa + " defensa)";
    }

}
