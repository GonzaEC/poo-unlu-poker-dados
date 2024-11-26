package com.edu.unlu.generala.modelos;

import java.util.ArrayList;
import java.util.List;

public class Partida {
    private List<Jugador> jugadores;
    private int turno;
    private int bote;

    public Partida() {
        this.jugadores = new ArrayList<>();
        this.turno = 0;
        this.bote =0;
    }
    public void agregarJugador(Jugador jugador){
        jugadores.add(jugador);
    }
    public void eliminarJugador(Jugador jugador){
        jugadores.remove(jugador);
    }
    public List<Jugador> obtenerTodosLosJugadores() {
        return jugadores;
    }
    public void avanzarTurno(){
        turno =(turno+1)% jugadores.size();
    }

    public void setBote(){
        int suma = 0;
        for (Jugador jugador: jugadores) {
            suma += jugador.getApostado();

        }
        this.bote = suma;
    }

}
