package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;

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
            case VER_HISTORIAL:
                verHistorial();
                break;
        }
    }


    private void verHistorial() {

    }

    private void iniciarPartida() {
        println("Iniciando partida: ");

    }

    private void registrarJugador(String entrada) {
        println(entrada);
        switch (estado){
            case ALTA_NOMBRE:
                nombre = entrada;
                estado = EstadoVistaConsola.ALTA_SALDO_INICIAL;
                println("Ingrese el saldo inicial: ");
                break;
            case ALTA_SALDO_INICIAL:
                try{
                    saldoInicial = Integer.parseInt(entrada);
                    boolean resultado = controlador.registrarJugadores(nombre, saldoInicial);
                    if (resultado){
                    println("Jugador registrado exitosamente:");
                    println("Nombre: " + nombre);
                    println("Saldo inicial: $" + saldoInicial);

                    mostrarJugadoresRegistrados();

                    estado = EstadoVistaConsola.ALTA_OTRO_JUGADOR;
                    println("Ingrese '1' para registrar otro jugador o 'N' para comenzar la partida:");
                    println("DEBUG: Estado actual = " + estado);
                    }else {
                    println("Error al registrar al jugador. Intente nuevamente.");
                    estado = EstadoVistaConsola.ALTA_NOMBRE;
                    }
                }catch (NumberFormatException e) {
                    println("Por favor, ingrese un número válido para el saldo inicial.");
                }
                break;
            case ALTA_OTRO_JUGADOR:
                entrada = entrada.trim();
                println("DEBUG: Entrada capturada = '" + entrada + "'");

                if (entrada.equals("1")) {
                    estado = EstadoVistaConsola.ALTA_NOMBRE;
                    println("Ingrese el nombre del siguiente jugador: ");
                } else if (entrada.equalsIgnoreCase("N")) {
                    println("Registro de jugadores finalizado. Comenzando la partida...");
                    estado = EstadoVistaConsola.INICIAR_PARTIDA;
                    iniciarPartida();
                } else {
                    println("Opción inválida. Ingrese '1' para registrar otro jugador o 'N' para iniciar la partida:");
                }
                break;
            default:
                println("Error: Estado inesperado al registrar jugador.");
                break;

        }
    }

    private void mostrarMenuPrincipal() {
        estado = EstadoVistaConsola.MENU_PRINCIPAL;
        println("-- MENÚ PRINCIPAL --");
        println("1. Iniciar nueva partida");
        println("2. Ver historial de partidas");
        println("3. Salir");
        print("Seleccione una opción: ");
    }
    private void procesarEntradaMenuPrincipal(String entrada) {
        println(entrada);
        switch (entrada) {
            case "1":
                estado = EstadoVistaConsola.ALTA_NOMBRE;
                println("Ingrese su nombre de jugador: ");
                break;
            case "2":
                verHistorialDePartidas();
                break;
            case "3":
                println("Gracias por jugar a la generala. ¡Hasta luego!");
                System.exit(0);
                break;
            default:
                println("Opción no válida. Por favor, elija una opción válida.");
        }
    }
    private void mostrarJugadoresRegistrados() {
        println("Jugadores registrados:");
        controlador.obtenerJugadores().forEach(jugador ->
                println(" - Nombre: " + jugador.getNombre() + ", Saldo: $" + jugador.getSaldo())
        );
    }
    private void verHistorialDePartidas() {
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
