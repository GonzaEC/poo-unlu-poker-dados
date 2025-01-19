package com.edu.unlu.generala.modelos;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Partida extends ObservableRemoto implements IPartida {
    private List<Jugador> jugadores;
    private Vaso vaso;
    private int turno;
    private int bote;

    public Partida() {
        this.jugadores = new ArrayList<>();
        this.vaso = new Vaso();
        this.turno = 0;
        this.bote =0;
    }

    public void iniciarPartida(){

    }

    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException{
        jugadores.add(jugador);
    }
    @Override
    public void eliminarJugador(Jugador jugador) throws RemoteException{
        jugadores.remove(jugador);
    }
    @Override
    public List<Jugador> obtenerTodosLosJugadores() throws RemoteException {
        return jugadores;
    }

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



    @Override
    public void avanzarTurno() throws RemoteException {
        turno =(turno+1)% jugadores.size();
        notificarObservadores();
    }

    @Override
    public void setBote() throws RemoteException {
        int suma = 0;
        for (Jugador jugador: jugadores) {
            suma += jugador.getApostado();

        }
        this.bote = suma;
        notificarObservadores();
    }

    @Override
    public int getTurno() throws RemoteException {
        return turno;
    }

    @Override
    public int getBote() throws RemoteException{
        return bote;
    }

    @Override
    public int cantidaJugadores() {
        int players = 0;
        for (Jugador jugador: jugadores) {
            players++;
        }
        return players;
    }


    public Vaso getVaso() {
        return this.vaso;
    }
}
