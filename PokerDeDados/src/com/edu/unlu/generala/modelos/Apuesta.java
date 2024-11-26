package com.edu.unlu.generala.modelos;

public class Apuesta {
    private Jugador apostador;
    private int cantidad;

    public Apuesta(Jugador jugador, int cantidad){
        this.apostador = jugador;
        this.cantidad = cantidad;
    }

    public Jugador getApostador() {
        return apostador;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setApuesta(int cantidad){
        this.cantidad = cantidad;

    }
}






