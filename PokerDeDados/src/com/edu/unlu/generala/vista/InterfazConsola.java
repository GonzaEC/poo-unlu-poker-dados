package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Apuesta;
import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazConsola extends JFrame implements IVista {
    private JPanel panelPrincipal;
    private JButton brnEnter;
    private JTextArea txtSalida;
    private JTextField txtEntrada;
    private javax.swing.JScrollPane JScrollPane;
    private EstadoVistaConsola estado;
    private String nombre;
    private int saldoInicial;
    private ControladorGenerala controlador;


    public InterfazConsola(ControladorGenerala controlador){
        setTitle("Consola Generala");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        this.controlador = controlador;


        brnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarEntrada(txtEntrada.getText());
                txtEntrada.setText("");
            }
        });

        mostrarMenuPrincipal();
    }
    private void print(String string) {
        txtSalida.append(string);
    }

    private void println(String string) {
        print(string + "\n");
    }


    private void procesarEntrada(String entrada) {
        switch (estado) {
            case MENU_PRINCIPAL:
                procesarEntradaMenuPrincipal(entrada);
                break;
            case ALTA_NOMBRE:
            case ALTA_SALDO_INICIAL:
                registrarJugador(entrada);
                break;
            case INICIAR_PARTIDA:
                iniciarPartida();
                break;
            default:
                println("Error: Estado inesperado. Reiniciando...");
                mostrarMenuPrincipal();
                break;
        }
    }
    private void mostrarMenuPrincipal() {
        estado = EstadoVistaConsola.MENU_PRINCIPAL;
        println("\n-- MENÚ PRINCIPAL --");
        println("1. Iniciar nueva partida");
        println("2. Ver historial de partidas");
        println("3. Salir");
        print("Seleccione una opción: ");
    }
    private void procesarEntradaMenuPrincipal(String entrada) {
        switch (entrada) {
            case "1":
                estado = EstadoVistaConsola.ALTA_NOMBRE;
                println("\nIngrese el nombre del primer jugador: ");
                break;
            case "2":
                verHistorialDePartidas();
                break;
            case "3":
                println("Gracias por jugar a la Generala. ¡Hasta pronto!");
                System.exit(0);
                break;
            default:
                println("Opción inválida. Intente nuevamente.");
        }
    }
    private void registrarJugador(String entrada) {
        switch (estado) {
            case ALTA_NOMBRE:
                nombre = entrada;
                println("Ingrese el saldo inicial para " + nombre + ": ");
                estado = EstadoVistaConsola.ALTA_SALDO_INICIAL;
                break;
            case ALTA_SALDO_INICIAL:
                try {
                    saldoInicial = Integer.parseInt(entrada);
                    if (controlador.registrarJugadores(nombre, saldoInicial)) {
                        println("\nJugador registrado: " + nombre + " con saldo inicial $" + saldoInicial);
                        estado = EstadoVistaConsola.ALTA_OTRO_JUGADOR;
                        println("Ingrese '1' para agregar otro jugador o 'N' para iniciar la partida: ");
                    } else {
                        println("Error al registrar el jugador. Intente nuevamente.");
                        estado = EstadoVistaConsola.ALTA_NOMBRE;
                    }
                } catch (NumberFormatException e) {
                    println("Saldo inválido. Ingrese un número entero.");
                }
                break;
            case ALTA_OTRO_JUGADOR:
                if (entrada.equalsIgnoreCase("1")) {
                    estado = EstadoVistaConsola.ALTA_NOMBRE;
                    println("\nIngrese el nombre del siguiente jugador:");
                } else if (entrada.equalsIgnoreCase("N")) {
                    println("\nRegistro de jugadores completado. ¡Comienza la partida!");
                    estado = EstadoVistaConsola.INICIAR_PARTIDA;
                    iniciarPartida();
                } else {
                    println("Opción inválida. Use '1' para agregar jugador o 'N' para continuar.");
                }
                break;
        }
    }
    private void iniciarPartida() {
        controlador.iniciarPartida();
        int ronda = 1;
        while (!controlador.juegoTerminado()) {
            println("\n--- RONDA " + ronda + " ---");
            mostrarSaldos();
            Apuesta apuesta = pedirApuesta();
            if (apuesta != null) {
                controlador.realizarApuesta(apuesta);
                mostrarResultadoDados(controlador.lanzarDados());
            }
            ronda++;
        }
        println("\n--- Fin del Juego ---");
        mostrarSaldos();
        println("Gracias por jugar. ¡Hasta la próxima!");
        mostrarMenuPrincipal();
    }
    @Override
    public void mostrarJugadoresRegistrados() {
        println("Jugadores registrados:");
        controlador.obtenerJugadores().forEach(jugador ->
                println(" - Nombre: " + jugador.getNombre() + ", Saldo: $" + jugador.getSaldo())
        );
    }
    private void verHistorialDePartidas() {
    }
    public void mostrarCartelRonda(int ronda){
        println("---------------------------");
        println("|          RONDA %d          |");
        println("---------------------------");
    }

    private void mostrarSaldos() {
        println("\n--- Saldos Restantes ---");
        for (Jugador jugador : controlador.obtenerJugadores()) {
            println(jugador.getNombre() + ": $" + jugador.getSaldo());
        }
    }
    public void mostrarApuestas() {
        println("\n--- Apuestas Realizadas ---");
        for (Apuesta apuesta : controlador.obtenerApuestas()) {
            println(apuesta.getApostador().getNombre() + " apostó $" + apuesta.getCantidad());
        }
    }
    @Override
    public Apuesta pedirApuesta() {
        return null;
    }

    @Override
    public void mostrarResultadoDados(int[] ints) {
        println("Resultados de los dados: " + ints);
    }

    @Override
    public int obtenerApuesta() {
        return 0;
    }

    @Override
    public void setControlador(ControladorGenerala controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        println(mensaje);
    }


    @Override
    public void iniciar() {
        setVisible(true);
    }
}
