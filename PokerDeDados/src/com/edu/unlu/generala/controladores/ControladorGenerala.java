package com.edu.unlu.generala.controladores;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import com.edu.unlu.generala.modelos.*;
import com.edu.unlu.generala.vista.IVista;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class ControladorGenerala implements IControladorRemoto {
    IVista vista;
    IPartida partida;
    public ControladorGenerala(){

    }
    public ControladorGenerala(IVista vista) {
        //this.partida = new Partida();
        this.vista = vista;
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

    public void agregarJugador(String jugador) {
        try {
            partida.agregarJugador(jugador);
        } catch (RemoteException e) {
            //vista
            e.printStackTrace();
        }
    }

    public int cantidadJugadores(){
        try {
            return partida.cantidaJugadores();
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
    public String evaluarJugada() {
       try {
           Jugador jugadorActual = getJugadorActual();
           int[] valoresDados = jugadorActual.getVasoJugador().getValores();
           int resultado = jugadorActual.getMano().verificarMano(valoresDados);

           switch (resultado) {
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
       } catch (Exception e) {
           e.printStackTrace();
       }

        return "";
    }

    public Jugador determinarGanador() {
        try {
            return partida.determinarGanador();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public boolean realizarApuesta(Jugador jugador, int monto) throws RemoteException {
        if (partida.getRondaActual() != EventoPartida.RONDA_APUESTAS) {
            vista.mostrarMensaje("No se puede apostar en esta ronda.");
            return false;
        }

        if (jugador.getSaldo() >= monto) {
            partida.agregarApuesta(jugador, monto);
            jugador.setHaApostado(true); // Ma
            return true;
        }
        if (jugador.getSaldo() < monto) {
            vista.mostrarMensaje("Saldo insuficiente para apostar $" + monto);
            return false;
        }
        return false;
    }

    public Jugador getJugadorActual() {
        try {
            return partida.getJugadorActual();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void cambiarTurno() throws RemoteException {
        partida.reiniciarTiradas();
        partida.avanzarTurno();
    }

    public List<Jugador> getJugadores() {
        try {
            return partida.getJugadores();
        }catch (Exception e){
            e.printStackTrace();
        }

        return List.of();
    }




    public int getTiradasRestantes() {
        try {
            return partida.getTiradasRestantes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<Jugador> obtenerPerdedores() throws RemoteException {
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
        try {
            jugadores = partida.getJugadores();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        for (Jugador gamer : jugadores) {
            if (gamer.getNombre().equals(nombreJugador)) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }

    // ! chequear creo que es al pedo
    public void depositar(String deposito) {
        Jugador player = null;
        try {
            player = partida.getJugadorActual();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            int monto = Integer.parseInt(deposito);
            if (monto > 0) {
                player.agregarSaldo(monto);
                vista.mostrarMensaje("Se depositaron $" + monto + " al jugador " + player.getNombre());
            } else {
                vista.mostrarMensaje("El monto debe ser mayor a 0. ");
            }
        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Entrada invalida, ingrese un numero valido.");
        }
    }

    public int pozoActual() throws RemoteException {
        return partida.getBote();
    }

    public int apuestaMaxima() {
        try {
            return partida.getApuestaMaxima();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int apuestaActual() {
        try {
            return partida.getJugadorActual().getApuesta().getCantidad();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void tirarTodosDados() throws RemoteException {

        if (partida.getRondaActual() != EventoPartida.RONDA_TIRADAS) {
            vista.mostrarMensaje("No se pueden tirar dados en esta ronda.");
            return;
        }
        Jugador jugador = null;
        try {
            jugador = partida.getJugadorActual();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (jugador.isPlantado()) {
            vista.mostrarMensaje("Ya te has plantado. No podés tirar más dados.");
            return;
        }
        try {
            if (partida.getTiradasRestantes() <= 0) {
                vista.mostrarMensaje("No quedan más tiradas.");
                return;
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            if (partida.getTiradasRestantes() == 2) {
                // Tirar todos los dados del vaso del jugador
                jugador.getVasoJugador().lanzarDados();
                // Actualizar mano poker con el vaso actual (que tiene los dados tirados)
                jugador.setManoPoker(jugador.getVasoJugador());
                partida.usarTirada();

                partida.notificarObservadores(EventoPartida.DADOS_TIRADOS);
                //vista.mostrarTirada(jugador.getVasoJugador().getDados());



            } else {
                vista.mostrarMensaje("Ya no puedes tirar todos los dados, usa la opción de dados seleccionados.");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }

    public void tirarDadosSeleccionados(List<Integer> indices) {
        try {
            if (partida.getRondaActual() != EventoPartida.RONDA_TIRADAS) {
                vista.mostrarMensaje("No se pueden tirar dados en esta ronda.");
                return;
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Jugador jugador = null;
        try {
            jugador = partida.getJugadorActual();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (jugador.isPlantado()) {
            vista.mostrarMensaje("Ya te has plantado. No podés tirar más dados.");
            return;
        }
        try {
            if (partida.getTiradasRestantes() <= 0) {
                vista.mostrarMensaje("No quedan más tiradas.");
                return;
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            if (partida.getTiradasRestantes() == 1) {
                // Tirar sólo los dados en el vaso según los indices recibidos
                jugador.getVasoJugador().lanzarSeleccionados(indices);

                // Mostrar los dados que fueron lanzados en esta tirada
                List<Dado> dadosTirados = new ArrayList<>();
                for (int i : indices) {
                    if (i >= 0 && i < jugador.getVasoJugador().getDados().size()) {
                        dadosTirados.add(jugador.getVasoJugador().getDados().get(i));
                    }
                }

                // Actualizar mano poker con el vaso modificado
                jugador.setManoPoker(jugador.getVasoJugador());
                partida.usarTirada();
                partida.notificarObservadores(EventoPartida.DADOS_TIRADOS);

            } else {
                vista.mostrarMensaje("No puedes tirar dados seleccionados en esta tirada.");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean todosLosJugadoresPlantados() {
        try {
            return partida.todosPlantados();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void plantarse() throws RemoteException {
        if (partida.getRondaActual() != EventoPartida.RONDA_TIRADAS) {
            vista.mostrarMensaje("No puedes plantarte en esta ronda.");
            return;
        }

        Jugador jugador = partida.getJugadorActual();
        jugador.setPlantado(true);
        vista.mostrarMensaje(jugador.getNombre() + " se ha plantado.");
        partida.reiniciarTiradas();

        if (partida.todosPlantados()) {
            //partida.setRondaActual(EventoPartida.RONDA_APUESTAS);
            //partida.notificarObservadores(EventoPartida.TODOS_PLANTADOS);
        } else {
            partida.avanzarTurno();
            partida.notificarObservadores(EventoPartida.ACTUALIZAR_MENU_DADOS);
        }

        /*if (partida.todosPlantados()) {
            iniciarRondaApuestas();
        } else {
            partida.avanzarTurno();
            vista.mostrarMensaje("Turno de " + partida.getJugadorActual().getNombre());
            vista.mostrarMenuJugadorDados();
        }*/
    }

    public void iniciarRondaApuestas() throws RemoteException {
        partida.setRondaActual(EventoPartida.RONDA_APUESTAS);// si usás enums o un flag para saber la etapa
        partida.reiniciarTurnoParaApuestas();
        vista.mostrarMensaje("¡Todos se han plantado! Comienza la ronda de apuestas.");
        vista.mostrarMenuApuestas(); // cambia el menú de la vista a modo apuestas
    }
    public boolean todosPlantados(){
        try {
            return partida.todosPlantados();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public int getPozo() throws RemoteException {
        return partida.getBote();
    }

    public int getApuestaMaxima() {
        try {
            return partida.getApuestaMaxima();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void igualarApuesta() throws RemoteException {
        int diferencia = partida.getApuestaMaxima() - partida.getJugadorActual().getApostado();
        partida.getJugadorActual().aumentarApuesta(diferencia);
        partida.agregarAlPozo(diferencia);

        evaluarFinDeRondaApuestas();

    }

    public void plantarseApuesta() throws RemoteException {
        partida.getJugadorActual().setPlantoApuesta(true);

        evaluarFinDeRondaApuestas();
    }

    private void evaluarFinDeRondaApuestas() throws RemoteException {
        boolean terminoRonda = partida.siguienteApostador();

        if (terminoRonda) {
            Jugador ganador = determinarGanador();
            try {
                partida.distribuirGanancias(ganador);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                partida.notificarObservadores(EventoPartida.GANADOR_DETERMINADO);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                partida.prepararSiguienteRonda();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            partida.notificarObservadores(EventoPartida.CAMBIO_TURNO);
        } else {
            partida.notificarObservadores(EventoPartida.RONDA_APUESTAS_INICIADA);
        }
    }

    public void subirApuesta(String entrada) throws RemoteException {
        Jugador jugador = getJugadorActual();

        try {
            int nuevoMonto = Integer.parseInt(entrada);
            int yaApostado = jugador.getApostado();

            if (nuevoMonto <= 0) {
                vista.mostrarMensaje("La apuesta debe ser mayor a 0.");
                partida.notificarObservadores(EventoPartida.RONDA_APUESTAS_INICIADA);
                return;
            }

            if (nuevoMonto <= yaApostado) {
                vista.mostrarMensaje("Tenés que subir la apuesta, no bajarla o repetirla.");
                partida.notificarObservadores(EventoPartida.RONDA_APUESTAS_INICIADA);
                return;
            }

            int diferencia = nuevoMonto - yaApostado;

            if (jugador.getSaldo() < diferencia) {
                vista.mostrarMensaje("No tenés suficiente saldo para subir esa cantidad.");
                partida.notificarObservadores(EventoPartida.RONDA_APUESTAS_INICIADA);
                return;
            }

            jugador.retirarSaldo(diferencia);
            jugador.apostar(diferencia); // Se suma al apostado
            partida.setApuestaMaxima(jugador.getApostado());
            partida.agregarAlPozo(diferencia);
            jugador.setPlantoApuesta(false); // Sigue participando

            evaluarFinDeRondaApuestas();

        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Entrada inválida. Debe ser un número.");
            partida.notificarObservadores(EventoPartida.RONDA_APUESTAS_INICIADA);
        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.partida = (IPartida) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object evento) throws RemoteException {
        if (evento instanceof EventoPartida) {
            switch (((EventoPartida) evento)) {
                case JUGADOR_AGREGADO -> {
                    vista.mostrarMensaje("Jugador agregado");
                }
                case JUEGO_INICIADO -> {
                    vista.mostrarMensaje("¡Comienza la partida!");
                    //vista.irAVentanaMenuDados();  // método que muestra o refresca la ventana de dados
                }
                case DADOS_TIRADOS -> {
                    vista.mostrarTirada(partida.getJugadorActual().getVasoJugador().getValores());

                }
                case ACTUALIZAR_MENU_DADOS -> {
                    vista.actualizarVista();
                    //vista.mostrarMenuJugadorDados(); // refresca/abre la ventana de dados
                }
                case TODOS_PLANTADOS -> {
                    vista.mostrarMensaje("¡Todos se plantaron! Comienza apuestas.");
                    vista.irAVentanaMenuApuestas();  // método que muestra o refresca la ventana de apuestas
                }
                case RONDA_APUESTAS_INICIADA -> {
                    vista.mostrarMenuApuestas();     // refresca la UI de apuestas
                }
                case APUESTA_IGUALADA, APUESTA_SUBIDA, APOSTADOR_PLANTADO -> {
                    // En todos estos casos simplemente actualizamos la UI de apuestas
                    //vista.actualizarMenuApuestas(partida.getJugadorActual());
                }
                case SIGUIENTE_APOSTADOR -> {
                    vista.mostrarMensaje("Siguiente turno para apostar");
                    //vista.actualizarMenuApuestas();
                }
                case RONDA_APUESTAS_FINALIZADA -> {
                    Jugador ganador = partida.determinarGanador();
                    vista.mostrarGanador();
                }
                case GANADOR_DETERMINADO -> {
                    vista.mostrarGanador();
                }
                default -> {
                    vista.mostrarMensaje("Evento desconocido: " + evento);
                }
            }
        }
    }
}
