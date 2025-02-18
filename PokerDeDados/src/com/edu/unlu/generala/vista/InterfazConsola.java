package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Apuesta;
import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

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


    private EstadoVistaConsola estadoActual = EstadoVistaConsola.MENU_PRINCIPAL;


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

    private void mostrarMenuPrincipal() {
        estado = EstadoVistaConsola.MENU_PRINCIPAL;
        println("\n-- MENÚ PRINCIPAL --");
        println("1. Iniciar nueva partida");
        println("2. Ver historial de partidas");
        println("3. Salir");
        print("Seleccione una opción: ");
    }

    private void procesarEntrada(String entrada){
        switch (estadoActual){
            case MENU_PRINCIPAL:
                procesarEntradaMenuPrincipal(entrada);
                break;
            case ALTA_JUGADOR:
                registrarJugador(entrada);
                break;
            case ALTA_APUESTA:
                realizarApuesta(entrada);
                break;
            case TIRAR_DADOS:
                tirarDados(entrada);
                break;
            case MANTENER_DADOS:
                mantenerDados(entrada);
                break;
            default:
                println("Estado inválido. Reiniciando...");
                estadoActual = EstadoVistaConsola.MENU_PRINCIPAL;
                mostrarMenuPrincipal();
                break;

        }
    }

    private void realizarApuesta(String entrada) {

    }

    private void procesarEntradaMenuPrincipal(String entrada) {
        switch (entrada) {
            case "1": // Registrar jugador
                estadoActual = EstadoVistaConsola.ALTA_JUGADOR;
                println("Ingrese el nombre del jugador: ");
                break;
            case "2": // Iniciar partida
                if (controlador.getJugadores().size() < 2) {
                    println("Se necesitan al menos 2 jugadores para iniciar.");
                } else {
                    try {
                        controlador.iniciar();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    estadoActual = EstadoVistaConsola.TIRAR_DADOS;
                    println(controlador.jugadorActual());
                }
                break;
            case "3": // Salir
                System.exit(0);
                break;
            default:
                println("Opción inválida. Intente nuevamente.");
                mostrarMenuPrincipal();
                break;
        }
    }
    private void registrarJugador(String nombre) {
        try {
            controlador.registrarJugador(nombre, 100); // Saldo inicial de 100
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        println("Jugador registrado: " + nombre);
        estadoActual = EstadoVistaConsola.MENU_PRINCIPAL;
        mostrarMenuPrincipal();
    }

    private void print(String string) {
        txtSalida.append(string);
    }

    private void println(String string) {
        print(string + "\n");
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
