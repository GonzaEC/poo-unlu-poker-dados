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
        if(partida.getJugadores().size() < 2){
            //notificar observador
            return;
        }
        //notificar inicio
        partida.avanzarTurno();

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
        Jugador ganador = null;
        int mejorPuntaje = -1;
        int [] mejoresDados = null;

        for (Jugador player : getJugadores()){
            int[] valoresDados = player.getVasoJugador().getValores();
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
            vista.mostrarMensaje("No quedan mas tiradas. ");
        }
    }
    public void tirarDados() {
        Jugador jugador = partida.getJugadorActual();

        if (partida.getTiradasRestantes() <= 0) {
            vista.mostrarMensaje("No quedan más tiradas.");
            return;
        }



        if (partida.getTiradasRestantes() == 2) {
            // Primera tirada: tirar los 5 dados
            jugador.getVasoJugador().lanzarDados();
            List<Dado> tirada = jugador.getVasoJugador().obtenerDados();
            jugador.setManoPoker(jugador.getVasoJugador());
            vista.mostrarTirada(tirada);
            partida.usarTirada();
        } else if (partida.getTiradasRestantes() == 1) {
            // Segunda tirada: tirar solo los dados no guardados
            List<Dado> guardados = jugador.getDadosGuardados();
            int dadosRestantes = 5 - guardados.size();

            if (dadosRestantes > 0) {
                vaso.lanzar(dadosRestantes);
                List<Dado> nuevos = vaso.obtenerDados();

                List<Dado> finalMano = new ArrayList<>(guardados);
                finalMano.addAll(nuevos);
                jugador.setManoActual(new ManoPoker(finalMano));
                vista.mostrarTirada(nuevos);
                vista.mostrarMensaje("Tirada final completada.");
            } else {
                vista.mostrarMensaje("No seleccionaste dados para volver a tirar.");
            }

            partida.usarTirada();
        }
    }
    public void tirarDadosSeleccion(ArrayList<Integer> indice){
        partida.tirarDadosSeleccion(indice);
        //notificar
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
        }

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

    public void avanzarTurno() {
        partida.avanzarTurno();
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
        ArrayList<Jugador> jugadores = new ArrayList<>();
        jugadores = partida.getJugadores();
        for (Jugador gamer : jugadores){
            if(gamer.getNombre().equals(nombreJugador)){
                resultado= true;
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
        for (Jugador j : partida.getJugadores()) {
            if (!j.isPlantado()) {
                return false;
            }
        }
        return true;
    }

    public void plantarse() {
        if (partida.getRondaActual() != Ronda.TIRADAS) {
            vista.mostrarMensaje("No puedes plantarte en esta ronda.");
            return;
        }

        Jugador jugador = partida.getJugadorActual();
        jugador.setPlantado(true);
        vista.mostrarMensaje(jugador.getNombre() + " se ha plantado.");

        if (partida.todosPlantados()) {
            partida.setRondaActual(Ronda.APOSTANDO);
            partida.reiniciarTurnoParaApuestas(); // si necesitás resetear el turno
            vista.mostrarMensaje("Todos se han plantado. Comienza la ronda de apuestas.");
        } else {
            partida.siguienteJugador();
            vista.mostrarMensaje("Turno de " + partida.getJugadorActual().getNombre());
        }
    }
    public void iniciarRondaApuestas() {
        partida.setRondaActual(Ronda.APOSTANDO); // si usás enums o un flag para saber la etapa
        vista.mostrarMenuApuestas(); // cambia el menú de la vista a modo apuestas
    }
}
