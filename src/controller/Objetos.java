package controller;

import java.util.LinkedList;

public class Objetos {
    private LinkedList<String> datos;
    public Objetos(){
        datos=new LinkedList<String>();
    }

    public LinkedList<String> getDatos() {
        return datos;
    }

    public void setDatos(LinkedList<String> datos) {
        this.datos = datos;
    }
}
