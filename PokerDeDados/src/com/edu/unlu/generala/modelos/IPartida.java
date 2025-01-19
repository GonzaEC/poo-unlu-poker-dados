package com.edu.unlu.generala.modelos;

import java.rmi.RemoteException;
import java.util.List;

public interface IPartida{
    void agregarJugador(Jugador jugador) throws RemoteException;

    void eliminarJugador(Jugador jugador) throws RemoteException;

    List<Jugador> obtenerTodosLosJugadores() throws RemoteException;

    void avanzarTurno() throws RemoteException;

    void setBote() throws RemoteException;

    int getTurno() throws RemoteException;

    int getBote() throws RemoteException;

    int cantidaJugadores();

    boolean sigueJuego();

    Vaso getVaso();
}
