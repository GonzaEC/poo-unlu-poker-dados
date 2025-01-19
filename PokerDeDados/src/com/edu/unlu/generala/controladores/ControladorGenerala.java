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
    public boolean realizarApuesta(Jugador jugador, int cantidad) {
        return partida.agregarApuesta(jugador, cantidad);
    }
    public List<Apuesta> obtenerApuestas() {
        return partida.getApuestas();
    }

    public void iniciarRonda() throws RemoteException {
        for (Jugador jugador : partida.obtenerTodosLosJugadores()) {
            // Solicita apuesta
            vista.mostrarMensaje(jugador.getNombre() + ", realiza tu apuesta:");
            int monto = vista.obtenerApuesta();
            jugador.setApostado(new Apuesta(jugador, monto));
        }

        // Luego realiza las tiradas
        for (Jugador jugador : partida.obtenerTodosLosJugadores()) {
            vista.mostrarMensaje(jugador.getNombre() + ", es tu turno para tirar los dados.");
            partida.getVaso().lanzarDados();
            vista.mostrarResultadoDados(partida.getVaso().obtenerValores());
        }

        // Determinar el ganador
        Jugador ganador = partida.determinarGanador();
        vista.mostrarMensaje("El ganador es: " + ganador.getNombre() + " y gana el bote de " + partida.getBote());
        partida.getApuesta().resetearApuestas();
    }

    public void inciarJuego() throws RemoteException {
        int catidadJugadores = partida.cantidaJugadores();

        Jugador jugadorActual;
        int ronda = partida.getTurno();
        boolean sigueJugando = true;
        while (partida.sigueJuego() || sigueJugando){
            vista.mostrarCartelRonda(ronda);
            //apuestas
            for (Jugador jugador : partida.obtenerTodosLosJugadores()) {
                Apuesta apostado = vista.pedirApuesta();
                jugador.setApostado(apostado);

            }
            //comienzoRonda
            for (Jugador jugador : partida.obtenerTodosLosJugadores()){
                vista.mostrarMensaje(jugador.getNombre() + " lanza los dados...");
                partida.getVaso().lanzarDados();
                int[] resultado = partida.getVaso().obtenerValores();
                
            }

        }
    }
    public List<Jugador> obtenerJugadores()  {
       try{
           return new ArrayList<Jugador>(partida.obtenerTodosLosJugadores());
       }catch (RemoteException e){
           throw new RuntimeException(e);
       }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.partida = (IPartida) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object cambio) throws RemoteException {
        if(cambio instanceof Integer){
            int indice = (Integer) cambio;
        }
    }
}
