package com.edu.unlu.generala.modelos;

import java.io.Serializable;

public class Apuesta implements Serializable {
    private int cantidad;
    private Jugador jugador;

    public Apuesta(Jugador jugador, int cantidad){
        this.jugador = jugador;
        this.cantidad = cantidad;
    }


    public int getCantidad() {
        return cantidad;
    }
    //chequear si esta el pedo... posiblemente crear una clase apuesta por cada apuesta realizada

    public void setApuesta(int cantidad){
        this.cantidad = cantidad;

    }
}






