package com.edu.unlu.generala.controladores;

import com.edu.unlu.generala.modelos.*;
import com.edu.unlu.generala.vista.IVista;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ControladorGenerala {
    private Partida partida;
    IVista vista;

    public ControladorGenerala(){
        this.partida = new Partida();
    }
    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void iniciar() throws RemoteException {
        if (partida.getJugadores().size() < 2) {
            vista.mostrarMensaje("Debe haber al menos 2 jugadores para comenzar la partida.");
            return;
        }

        vista.mostrarMensaje("¡Comienza la partida!");
        partida.setRondaActual(EventoPartida.RONDA_TIRADAS);
        partida.reiniciarTiradas(); // Asegurarse de que todos tienen 2 tiradas
        Jugador jugadorActual = partida.getJugadorActual();
        vista.mostrarMensaje("Turno de " + jugadorActual.getNombre());

    }

    public void agregarJugador(String jugador){
        try {
            partida.agregarJugador(jugador);
        }catch (RemoteException e){
            //vista
            e.printStackTrace();
        }
    }


    public String evaluarJugada(){
        Jugador jugadorActual = getJugadorActual();
        int[] valoresDados= jugadorActual.getVasoJugador().getValores();
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
        return partida.determinarGanador();
    }


    public boolean realizarApuesta(Jugador jugador, int monto){
        if (partida.getRondaActual() != EventoPartida.RONDA_APUESTAS) {
            vista.mostrarMensaje("No se puede apostar en esta ronda.");
            return false;
        }

        if (jugador.getSaldo() >= monto) {
            partida.agregarApuesta(jugador, monto);
            jugador.setHaApostado(true); // Ma
            return true;
        }
        return false;
    }
    public Jugador getJugadorActual(){
        return partida.getJugadorActual();
    }

    public void cambiarTurno(){
        partida.reiniciarTiradas();
        partida.avanzarTurno();
    }
    public List<Jugador> getJugadores(){
        return partida.getJugadores();
    }

    public void finalizarPartida(){
        Jugador ganador = partida.determinarGanador();
        //notificar
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
        }

        return perdedores;
    }

    public boolean todosHanApostado() {
        for (Jugador jugador : getJugadores()) {
            if (!jugador.haApostado()) { // Verificar si el jugador ha apostado
                return false;
            }
        }
        return true;
    }

    public boolean existeJugador(String nombreJugador) {
        boolean resultado = false;
        List<Jugador> jugadores = new ArrayList<>();
        jugadores = partida.getJugadores();
        for (Jugador gamer : jugadores){
            if (gamer.getNombre().equals(nombreJugador)) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }

    // ! chequear creo que es al pedo
    public void depositar(String deposito) {
        Jugador player = partida.getJugadorActual();
        try {
            int monto = Integer.parseInt(deposito);
            if (monto > 0){
                player.agregarSaldo(monto);
                vista.mostrarMensaje("Se depositaron $" + monto + " al jugador " + player.getNombre());
            }else{
                vista.mostrarMensaje("El monto debe ser mayor a 0. ");
            }
        }catch(NumberFormatException e){
            vista.mostrarMensaje("Entrada invalida, ingrese un numero valido.");
        }
    }

    public int pozoActual() {
        return partida.getBote();
    }

    public int apuestaMaxima() {
        return partida.getApuestaMaxima();
    }

    public int apuestaActual() {
        return partida.getJugadorActual().getApuesta().getCantidad();
    }

    public void tirarTodosDados() {

        if (partida.getRondaActual() != EventoPartida.RONDA_TIRADAS) {
            vista.mostrarMensaje("No se pueden tirar dados en esta ronda.");
            return;
        }
        Jugador jugador = partida.getJugadorActual();
        if (jugador.isPlantado()) {
            vista.mostrarMensaje("Ya te has plantado. No podés tirar más dados.");
            return;
        }
        if (partida.getTiradasRestantes() <= 0) {
            vista.mostrarMensaje("No quedan más tiradas.");
            return;
        }

        if (partida.getTiradasRestantes() == 2) {
            // Tirar todos los dados del vaso del jugador
            jugador.getVasoJugador().lanzarDados();
            vista.mostrarTirada(jugador.getVasoJugador().obtenerDados());

            // Actualizar mano poker con el vaso actual (que tiene los dados tirados)
            jugador.setManoPoker(jugador.getVasoJugador());

            partida.usarTirada();
        } else {
            vista.mostrarMensaje("Ya no puedes tirar todos los dados, usa la opción de dados seleccionados.");
        }
    }

    public void tirarDadosSeleccionados(List<Integer> indices) {
        if (partida.getRondaActual() != EventoPartida.RONDA_TIRADAS) {
            vista.mostrarMensaje("No se pueden tirar dados en esta ronda.");
            return;
        }
        Jugador jugador = partida.getJugadorActual();
        if (jugador.isPlantado()) {
            vista.mostrarMensaje("Ya te has plantado. No podés tirar más dados.");
            return;
        }
        if (partida.getTiradasRestantes() <= 0) {
            vista.mostrarMensaje("No quedan más tiradas.");
            return;
        }

        if (partida.getTiradasRestantes() == 1) {
            // Tirar sólo los dados en el vaso según los índices recibidos
            jugador.getVasoJugador().lanzarSeleccionados(indices);

            // Mostrar los dados que fueron lanzados en esta tirada
            List<Dado> dadosTirados = new ArrayList<>();
            for (int i : indices) {
                if (i >= 0 && i < jugador.getVasoJugador().getDados().size()) {
                    dadosTirados.add(jugador.getVasoJugador().getDados().get(i));
                }
            }
            vista.mostrarTirada(dadosTirados);

            // Actualizar mano poker con el vaso modificado
            jugador.setManoPoker(jugador.getVasoJugador());

            partida.usarTirada();

        } else {
            vista.mostrarMensaje("No puedes tirar dados seleccionados en esta tirada.");
        }
    }
    private boolean todosLosJugadoresPlantados() {
        return partida.todosPlantados();
    }

    public void plantarse() {
        if (partida.getRondaActual() != EventoPartida.RONDA_TIRADAS) {
            vista.mostrarMensaje("No puedes plantarte en esta ronda.");
            return;
        }

        Jugador jugador = partida.getJugadorActual();
        jugador.setPlantado(true);
        vista.mostrarMensaje(jugador.getNombre() + " se ha plantado.");

        if (partida.todosPlantados()) {
            iniciarRondaApuestas();
        } else {
            partida.avanzarTurno();
            vista.mostrarMensaje("Turno de " + partida.getJugadorActual().getNombre());
        }
    }
    public void iniciarRondaApuestas() {
        partida.setRondaActual(EventoPartida.RONDA_APUESTAS);// si usás enums o un flag para saber la etapa
        partida.reiniciarTurnoParaApuestas();
        vista.mostrarMensaje("¡Todos se han plantado! Comienza la ronda de apuestas.");
        vista.mostrarMenuApuestas(); // cambia el menú de la vista a modo apuestas
    }

    public int getPozo() {
        return partida.getBote();
    }

    public int getApuestaMaxima() {
        return partida.getApuestaMaxima();
    }

    public void igualarApuesta() {
        int diferencia = partida.getApuestaMaxima() - partida.getJugadorActual().getApostado();
        partida.getJugadorActual().aumentarApuesta(diferencia);
        partida.agregarAlPozo(diferencia);

        evaluarFinDeRondaApuestas();

    }

    public void plantarseApuesta() {
        partida.getJugadorActual().setPlantoApuesta(true);

        evaluarFinDeRondaApuestas();
    }

    private void evaluarFinDeRondaApuestas() {
        boolean terminoRonda = partida.siguienteApostador();

        if (terminoRonda) {
            Jugador ganador = determinarGanador();
            partida.distribuirGanancias(ganador);
            vista.mostrarGanador();
            partida.avanzarTurno();
            vista.mostrarMenuJugadorDados();
        } else {
            vista.mostrarMenuApuestas();
        }
    }

}
