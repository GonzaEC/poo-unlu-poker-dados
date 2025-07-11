package com.edu.unlu.generala.modelos;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface IPartida extends IObservableRemoto {
    void agregarJugador(String jugador) throws RemoteException;


    void avanzarTurno() throws RemoteException;

    int getBote() throws RemoteException;

    int cantidaJugadores() throws RemoteException;

    //tirar dados seleccionados
    Jugador determinarGanador() throws RemoteException;

    boolean agregarApuesta(Jugador jugador, int cantidad) throws RemoteException;


    List<Jugador> getJugadores() throws RemoteException;

    Jugador getJugadorActual() throws RemoteException;


    int getTiradasRestantes() throws RemoteException;

    void usarTirada() throws RemoteException;

    void reiniciarTiradas() throws RemoteException;

    void agregarAlPozo(int diferencia) throws RemoteException;

    void setApuestaMaxima(int apostado) throws RemoteException;

    void distribuirGanancias(Jugador ganador) throws RemoteException;

    void prepararSiguienteRonda() throws RemoteException;

    boolean siguienteApostador() throws RemoteException;

    int getApuestaMaxima() throws RemoteException;

    boolean todosPlantados() throws RemoteException;

    void reiniciarTurnoParaApuestas() throws RemoteException;

    void setRondaActual(EventoPartida eventoPartida) throws RemoteException;

    EventoPartida getRondaActual() throws RemoteException;
}
