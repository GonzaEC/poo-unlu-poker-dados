package com.edu.unlu.generala.controladores;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import com.edu.unlu.generala.modelos.Apuesta;
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

    public void iniciar() throws RemoteException {
        if(partida.getJugadores().size() < 2){
            //notificar observador
            return;
        }
        //notificar inicio
        partida.avanzarTurno();

    }


    public String evaluarJugada(){
        Jugador jugadorActual = getJugadorActual();
        int[] valoresDados= jugadorActual.getVasoJugador().obtenerValores();
        int resultado = jugadorActual.getMano().verificarMano(valoresDados);

        switch (resultado){
            case 7:
                return "Poker real";
            case 6:
                return "Poker cuadruple";
            case 5:
                return "Full";
            case 4:
                return "Escalera mayor";
            case 3:
                return "Escalera menor";
            case 2:
                return "Piernas";
            case 1:
                return "Pares dobles";
            case 0:
                return "Pares";
            default:
                    return "Sin valor";
        }
    }

    public Jugador determinarGanador(){
        Jugador ganador = null;
        int mejorPuntaje = -1;
        int [] mejoresDados = null;

        for (Jugador player : getJugadores()){
            int[] valoresDados = player.getVasoJugador().obtenerValores();
            int puntajeActual = player.getMano().verificarMano(valoresDados);

            if (puntajeActual > mejorPuntaje){
                mejorPuntaje = puntajeActual;
                ganador = player;
                mejoresDados= valoresDados;
                
            } else if (puntajeActual == mejorPuntaje) {
                int desempate = player.getMano().desempatar(valoresDados, mejoresDados);
                if(desempate> 0){
                    ganador = player;
                    mejoresDados = valoresDados;
                }
            }

        }
        return ganador;
    }


    public boolean realizarApuesta(Jugador jugador, int monto){
        return partida.agregarApuesta(jugador,monto);
    }
    public Jugador getJugadorActual(){
        return partida.getJugadorActual();
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
        if (partida.getTiradasRestantes() > 0){
            partida.tirarDados();
            partida.usarTirada();
        }else {
            vista.mostrarMensaje("No quedan mas tiradas en este turno!");
        }
    }

    public void tirarDadosSeleccion(ArrayList<Integer> indice){
        partida.tirarDadosSeleccion(indice);
        //notificar
    }

    public void cambiarTurno(){
        try {
            partida.reiniciarTiradas();
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

    public int getTiradasRestantes(){
        return partida.getTiradasRestantes();
    }

    public List<Jugador> obtenerPerdedores() {
        List<Jugador> perdedores = new ArrayList<>();

        for (Jugador jugador : partida.getJugadores()) {
            if (jugador.getSaldo() <= 0) {
                perdedores.add(jugador);
            }
        }                                                                                                                                                                                                                                                           zzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

        return perdedores;
    }

    public String getGanador(){
        String nombre = null;
        Jugador ganador = partida.determinarGanador();
        nombre = ganador.getNombre();
        return nombre;
    }

    public void reiniciarPartida() throws RemoteException {
        for (Jugador jugador : getJugadores()) {
            jugador.getVasoJugador().lanzarDados();
        }

        partida.getApuestas().clear();

        partida.setBote(0);
        partida.setTurno(0);
    }

}
