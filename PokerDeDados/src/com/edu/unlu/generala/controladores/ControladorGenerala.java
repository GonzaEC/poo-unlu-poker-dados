package com.edu.unlu.generala.controladores;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import com.edu.unlu.generala.modelos.Apuesta;
import com.edu.unlu.generala.modelos.IPartida;
import com.edu.unlu.generala.modelos.Jugador;
import com.edu.unlu.generala.modelos.Partida;
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

    public void registrarJugador(Jugador player){
        partida.agregarJugador(player);
        //notificar
    }

    public void realizarApuestas(Jugador player, int monto){
        if(partida.agregarApuesta()){
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
        partida.avanzarTurno();
        //notificar
    }

    public void finalizarPartida(){
        Jugador ganador = partida.determinarGanador();
        //notificar
    }


}
