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
    public void registrarJugadores(String nombre, int saldoInicial) throws RemoteException {
        Jugador jugador = new Jugador(nombre, saldoInicial);
        try{
            this.partida.agregarJugador(jugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
    public void seleccionarTiros(List<Integer> indices){
        if (indices == null || indices.isEmpty()){
            //aviso de se planta

        }
        else {
            partida.tirarDadosSeleccion((ArrayList<Integer>) indices);
        }
    }


}
