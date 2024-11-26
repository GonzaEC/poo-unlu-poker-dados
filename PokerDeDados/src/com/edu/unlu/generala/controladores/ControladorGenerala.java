package com.edu.unlu.generala.controladores;

import com.edu.unlu.generala.modelos.Jugador;
import com.edu.unlu.generala.modelos.Partida;
import com.edu.unlu.generala.vista.IVista;

import java.util.List;

public class ControladorGenerala {
    private Partida partida;
    IVista vista;

    public void setVista(IVista vista) {
        this.vista = vista;
    }
    public ControladorGenerala(){
        this.partida = new Partida();
    }

    public boolean registrarJugadores(String nombre, int saldoInicial){
        Jugador jugador = new Jugador(nombre, saldoInicial);
        partida.agregarJugador(jugador);
        return true;
    }
    public void inciarPartida(){

    }
    public List<Jugador> obtenerJugadores() {
       return partida.obtenerTodosLosJugadores();
    }
}
