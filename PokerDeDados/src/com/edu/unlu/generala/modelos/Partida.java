package com.edu.unlu.generala.modelos;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Partida extends ObservableRemoto implements IPartida {
    private final List<Jugador> jugadores;
    private final List<Apuesta> apuestas;
    private Vaso vaso;
    private int indiceJugadorActual;
    private int bote;
    private Jugador jugadorActual;
    private int tiradasRestantes;
    private static final int MAX_TIRADAS = 2;

    private EventoPartida rondaActual;
    private int apuestaMaxima;


    public Partida() {
        this.jugadores = new ArrayList<>();
        this.apuestas = new ArrayList<>();
        this.vaso = new Vaso();
        this.indiceJugadorActual = 0;
        this.bote = 0;
        jugadorActual = null;
        this.tiradasRestantes = MAX_TIRADAS;
        this.rondaActual = EventoPartida.RONDA_TIRADAS;
        this.apuestaMaxima = 0;
    }

    public void setIndiceJugadorActual(int indiceJugadorActual) {
        this.indiceJugadorActual = indiceJugadorActual;
    }

    public void setBote(int bote) {
        this.bote = bote;
    }
    public int getTiradasRestantes() {
        return tiradasRestantes;
    }
    //gets
    @Override
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    @Override
    public List<Apuesta> getApuestas() {
        return apuestas;
    }

    @Override
    public Vaso getVaso() {
        return vaso;
    }

    @Override
    public int getTurno() {
        return indiceJugadorActual;
    }

    @Override
    public int getBote() {
        return bote;
    }

    @Override
    public Jugador getJugadorActual() {
        return jugadores.get(indiceJugadorActual);
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
    public void agregarJugador(String nombre) throws RemoteException {
        jugadores.add(new Jugador(nombre, 100));

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
            jugador.apostar(cantidad);
            apuestas.add(jugador.getApuesta());
            bote += cantidad;
            if (cantidad > apuestaMaxima) {
                apuestaMaxima = cantidad;
            }
            return true;
        }
        return false;
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
        int[] mejoresDados = null;

        for (Jugador player : jugadores) {
            int[] valoresDados = player.getVasoJugador().getValores();
            int puntajeActual = player.getMano().verificarMano(valoresDados);

            if (puntajeActual > mejorPuntaje) {
                mejorPuntaje = puntajeActual;
                ganador = player;
                mejoresDados = valoresDados;

            } else if (puntajeActual == mejorPuntaje) {
                int desempate = player.getMano().desempatar(valoresDados, mejoresDados);
                if (desempate > 0) {
                    ganador = player;
                    mejoresDados = valoresDados;
                }
            }
        }

        return ganador;
    }

    public void avanzarTurno() {
        indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
    }

    public int getApuestaMaxima() {
        return apuestaMaxima;
    }
    public void reiniciarPlantados() {
        for (Jugador j : this.getJugadores()) {
            j.setPlantado(false);
        }
    }
    public EventoPartida getRondaActual() {
        return rondaActual;
    }

    public void setRondaActual(EventoPartida rondaActual) {
        this.rondaActual = rondaActual;
    }

    public boolean todosPlantados() {
        for (Jugador j : jugadores) {
            if (!j.isPlantado()) {
                return false;
            }
        }
        return true;

    }
    public void avanzarRondaSiEsNecesario() {
        if (rondaActual == EventoPartida.RONDA_TIRADAS && todosPlantados()) {
            rondaActual = EventoPartida.RONDA_APUESTAS;
            reiniciarTiradas(); // opcional si querés resetear
        }
    }

    public void reiniciarTurnoParaApuestas() {
        this.indiceJugadorActual = 0;
        for (Jugador j : jugadores) {
            j.setHaApostado(false);
        }
    }

    public void agregarAlPozo(int diferencia) {
        this.bote = bote+=diferencia;
    }

    public boolean siguienteApostador() {
        int totalJugadores = jugadores.size();
        int intentos = 0;

        do {
            indiceJugadorActual = (indiceJugadorActual + 1) % totalJugadores;
            intentos++;
        } while ((jugadores.get(indiceJugadorActual).isPlantoApuesta() ||
                jugadores.get(indiceJugadorActual).getApostado() == apuestaMaxima)
                && intentos <= totalJugadores);

        // Verificamos si todos se plantaron o igualaron
        boolean rondaFinalizada = jugadores.stream().allMatch(j ->
                j.isPlantoApuesta() || j.getApostado() == apuestaMaxima
        );

        if (rondaFinalizada) {
            avanzarTurno(); // Cambia el estado de la partida si es necesario
        }

        return rondaFinalizada; // Le dice al controlador si la ronda de apuestas terminó
    }

    public void distribuirGanancias(Jugador ganador) {
        ganador.agregarSaldo(bote);
        this.bote= 0;
    }
}
    //finalizar partida
}
