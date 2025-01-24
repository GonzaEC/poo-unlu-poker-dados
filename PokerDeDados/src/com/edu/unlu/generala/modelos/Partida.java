package com.edu.unlu.generala.modelos;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Partida extends ObservableRemoto implements IPartida {
    private ArrayList<Jugador> jugadores;
    private ArrayList<Apuesta> apuestas;
    private Vaso vaso;
    private int turno;
    private int bote;
    private Jugador jugadorActual;


    public Partida() {
        this.jugadores = new ArrayList<>();
        this.apuestas = new ArrayList<>();
        this.vaso = new Vaso();
        this.turno = 0;
        this.bote = 0;
    }

    @Override
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
<<<<<<< HEAD
=======

    public boolean sigueJuego(){

        int contadorJugadoresEnJuego=0;

        for (Jugador jugadore : jugadores) {
            if (jugadore.getApostado() > 0) {
                contadorJugadoresEnJuego++;
            } else {
                try {
                    eliminarJugador(jugadore);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return contadorJugadoresEnJuego >= 2;
    }


>>>>>>> 4f647154d506e1c302743dbb5b22eb07ddd07968

    @Override
    public ArrayList<Apuesta> getApuestas() {
        return apuestas;
    }

    @Override
    public Vaso getVaso() {
        return vaso;
    }

    @Override
    public int getTurno() {
        return turno;
    }

    @Override
    public int getBote() {
        return bote;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

<<<<<<< HEAD
    public void avanzarTurno(){
        turno = (turno + 1) % jugadores.size();
        jugadorActual = jugadores.get(turno);

=======

    public Vaso getVaso() {
        return this.vaso;
>>>>>>> 4f647154d506e1c302743dbb5b22eb07ddd07968
    }
    //Agregar jugadores

    public void agregarJugadores(Jugador player){
            jugadores.add(player);

    }

    //realizar apuestas
    public boolean realizarApuesta(Jugador player, int monto){
        if(player.retirarSaldo(monto)){
            apuestas.add(new Apuesta(player, monto));
            bote += monto;
            return true;
        }
        else {
            return false;
        }



    }
    //tirar dados
    public void tirarDados() {
        vaso.lanzarDados();
    }
    public void tirarDadosSeleccion(ArrayList<Integer> indices){
        vaso.lanzarSeleccionados(indices);
    }

    //revisar jugada
    public Jugador determinarGanador() {
        Jugador ganador = null;
        int mejorPuntaje = -1;
        int[] mejorMano = null;

        for (Jugador jugador : this.jugadores) {
            int[] valoresDados = jugador.get().getValoresDados();
            int puntajeActual = jugador.getManoPoker().obtenerPuntaje(valoresDados);

            if (puntajeActual > mejorPuntaje) {
                mejorPuntaje = puntajeActual;
                ganador = jugador;
                mejorMano = valoresDados;
            } else if (puntajeActual == mejorPuntaje) {
                // Desempate
                if (jugador.getManoPoker().desempatar(valoresDados, mejorMano) > 0) {
                    ganador = jugador;
                    mejorMano = valoresDados;
                }
            }
        }
        return ganador;
    }

    //determinar ganador

    //finalizar partida
}