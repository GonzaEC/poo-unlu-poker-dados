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
    private int tiradasRestantes;
    private static final int MAX_TIRADAS = 2;



    public Partida() {
        this.jugadores = new ArrayList<>();
        this.apuestas = new ArrayList<>();
        this.vaso = new Vaso();
        this.turno = 0;
        this.bote = 0;
        jugadorActual = null;
        this.tiradasRestantes = MAX_TIRADAS;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public void setBote(int bote) {
        this.bote = bote;
    }
    public int getTiradasRestantes() {
        return tiradasRestantes;
    }
    //gets
    @Override
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

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

    @Override
    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    //metodos clase
    @Override
    public int cantidaJugadores() {
        return jugadores.size();
    }

    @Override
    public boolean sigueJuego() {
        return jugadores.size() > 2 && determinarGanador() == null;
    }

    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException {
        jugadores.add(jugador);

    }

    @Override
    public void eliminarJugador(Jugador jugador) throws RemoteException {
        jugadores.remove(jugador);
    }

    @Override
    public List<Jugador> obtenerTodosLosJugadores() throws RemoteException {
        return new ArrayList<>(jugadores);
    }

    public void usarTirada() {
        if (tiradasRestantes > 0) {
            tiradasRestantes--;
        }
    }
    public void reiniciarTiradas() {
        this.tiradasRestantes = MAX_TIRADAS;
    }
    @Override
    public boolean agregarApuesta(Jugador jugador, int cantidad) {
        if (jugador.retirarSaldo(cantidad)){
            apuestas.add(new Apuesta(jugador, cantidad));
            bote += cantidad;
            return true;
        }
        return false;
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
    //tirar dados seleccionados
    @Override
    public void tirarDadosSeleccion(ArrayList<Integer> indices){
        vaso.lanzarSeleccionados(indices);
    }
    //determina ganador con mayor mano
    public Jugador determinarGanador() {
        Jugador ganador = null;
        int mejorPuntaje = -1;
        int[] mejorMano = null;

        for (Jugador jugador : this.jugadores) {
            int[] valoresDados = jugador.getVasoJugador().obtenerValores();
            int puntajeActual = jugador.getMano().verificarMano(valoresDados);

            if (puntajeActual > mejorPuntaje) {
                mejorPuntaje = puntajeActual;
                ganador = jugador;
                mejorMano = valoresDados;
            } else if (puntajeActual == mejorPuntaje) {
                // Desempate
                if (jugador.getMano().desempatar(valoresDados, mejorMano) > 0) {
                    ganador = jugador;
                    mejorMano = valoresDados;
                }
            }
        }
        return ganador;
    }

    public void avanzarTurno() {
        turno = (turno + 1) % jugadores.size();
        jugadorActual = jugadores.get(turno);
    }

    //finalizar partida
}
