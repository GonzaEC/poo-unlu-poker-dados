package com.edu.unlu.generala.controladores;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import com.edu.unlu.generala.modelos.IPartida;
import com.edu.unlu.generala.modelos.Jugador;
import com.edu.unlu.generala.vista.IVista;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ControladorGenerala implements IControladorRemoto {
    private IPartida partida;
    IVista vista;

    public ControladorGenerala(){
        //this.partida = new Partida();
    }
    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void iniciar(){
        if(partida.getJugadores().size() < 2){
            //notificar observador
            return;
        }
        //notificar inicio
        partida.avanzarTurno();

    }

    public void registrarJugador(String nombre, int saldo) throws RemoteException {
        Jugador player = new Jugador(nombre, saldo);
        partida.agregarJugador(player);
        //notificar
    }

    public void realizarApuestas(Jugador player, int monto){
        if(partida.agregarApuesta(player, monto)){
            //notificar
        }
    }

    public void tirarDados(){
        partida.tirarDados();
        //notificar
    }

    public void tirarDadosSeleccion(ArrayList<Integer> indice){
        partida.tirarDadosSeleccion(indice);
        //notificar
    }

    public void cambiarTurno(){
        try {
            partida.avanzarTurno();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public List<Jugador> getJugadores(){
        return partida.getJugadores();
    }

    public void finalizarPartida(){
        Jugador ganador = partida.determinarGanador();
        //notificar
    }

    public String jugadorActual(){
        String nombre = null;
        Jugador jugadorActual = partida.getJugadorActual();
        nombre = jugadorActual.getNombre();
        return nombre;
    }

    public List<Jugador> obtenerPerdedores() {
        List<Jugador> perdedores = new ArrayList<>();

        for (Jugador jugador : partida.getJugadores()) {
            if (jugador.getSaldo() <= 0) {
                perdedores.add(jugador);
            }
        }

        return perdedores;
    }
    public String getGanador(){
        String nombre = null;
        Jugador ganador = partida.determinarGanador();
        nombre = ganador.getNombre();
        return nombre;
    }
}
