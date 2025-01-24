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
    //chequear si esta el pedo... posiblemente crear una clase apuesta por cada apuesta realizada
    public void setApuesta(int cantidad){
        this.cantidad = cantidad;

    }
}






