package com.edu.unlu.generala.modelos;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IPartida{
    void agregarJugador(Jugador jugador) throws RemoteException;

    void eliminarJugador(Jugador jugador) throws RemoteException;

    List<Jugador> obtenerTodosLosJugadores() throws RemoteException;

    void avanzarTurno() throws RemoteException;

    int getTurno() throws RemoteException;

    int getBote() throws RemoteException;

    int cantidaJugadores();

    boolean sigueJuego();

    Vaso getVaso();

    //tirar dados seleccionados
    void tirarDadosSeleccion(ArrayList<Integer> indices);

    Jugador determinarGanador();

    boolean agregarApuesta(Jugador jugador, int cantidad);

    List<Apuesta> getApuestas();

    List<Jugador> getJugadores();

    Jugador getJugadorActual();

    void tirarDados();
}
