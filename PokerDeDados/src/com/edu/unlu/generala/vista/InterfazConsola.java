package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
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

    private EstadoVistaConsola estadoActual;
    private ControladorGenerala controlador;
    private String nombreJugador;



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
                enviarTexto();
            }
        });

        mostrarMenuPrincipal();

    }


    private void enviarTexto() {
        println(txtEntrada.getText());
        try {
            procesarEntrada(txtEntrada.getText());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        txtEntrada.setText("");
    }
    private void procesarEntrada(String entrada){
        switch (estadoActual){
            case MENU_PRINCIPAL:
                limpiarPantalla();
                procesarEntradaMenuPrincipal(entrada);
                break;
            case AGREGAR_JUGADOR:
                agregarJugador();
                break;
            case MENU_JUGADOR_DADOS:

                procesarEntradaMenuJugadorDados(entrada);
                break;
            case DEPOSITAR:
                //depositarSaldo(entrada);
                break;
            case TIRAR_DADOS:

                break;
        }
    }
    // ! creo que no sirve
    private void depositarSaldo(String entrada) {
        controlador.depositar(entrada);
    }

    private void limpiarPantalla() {
        txtSalida.setText("");
    }

    private void agregarJugador() {
        if(!txtEntrada.getText().isEmpty()) {
            nombreJugador= txtEntrada.getText();
            if(!controlador.existeJugador(nombreJugador)){
                controlador.agregarJugador(nombreJugador);
                println(" Jugador agregado con exito");
                mostrarMenuPrincipal();
            }
            else{
                println(" Ese jugador ya se encuentra registrado, ingrese otro : ");
            }
        }
        else{
            println(" Ingrese un nombre valido: ");
        }

    }

    private void procesarEntradaMenuPrincipal(String entrada) {
        switch (entrada) {
            case "1":
                //agregar jugador
                println("Nombre del jugador: ");
                estadoActual = EstadoVistaConsola.AGREGAR_JUGADOR;
                break;
            case "2":
                println("Lista de jugadores:");
                for (Jugador j : controlador.getJugadores()) {
                    println(" - " + j.getNombre());
                }
                mostrarMenuPrincipal(); // volver al menú
                break;
            case "3":
                controlador.iniciar();
                mostrarMenuJugadorDados();
                break;
            case "0":
                System.exit(0);
                break;

            default:
                println("Opcion invalida, vuelva a ingresar");
                mostrarMenuPrincipal();
        }

    }
    private void procesarEntradaMenuJugadorDados(String entrada) {
        switch (entrada){
            case "1":
                controlador.tirarDados();
                mostrarMenuJugadorDados();
                break;
            case "2":

                break;
            case "3":

                break;
            case "4":

                break;


        }
    }
    private void mostrarMenuJugadorDados() {
        estadoActual = EstadoVistaConsola.MENU_JUGADOR_DADOS;
        println("\n-- TURNO DE: " + controlador.getJugadorActual().getNombre() + " --");
        println("== MENÚ DE JUGADAS ==");
        println("1. Tirar todos los dados");
        println("2. Apartar dados y volver a tirar los restantes");
        println("3. Plantarse (no tirar más)");
        println("Seleccione una opción:");
    }
    private void mostrarMenuJugadorApuestas() {
        estadoActual = EstadoVistaConsola.MENU_JUGADOR_APUESTAS;
        println("\n--- FASE DE APUESTAS ---");
        println("\n-- TURNO DE: " + controlador.getJugadorActual().getNombre() + " --");
        println("Tu apuesta actual: $" + controlador.apuestaActual());
        println("Apuesta máxima: $" + controlador.apuestaMaxima());
        println("Pozo actual: $" + controlador.pozoActual());
        println("== MENÚ DE APUESTAS ==");
        println("1. Apostar ");
        println("2. Igualar apuesta");
        println("3. Subir apuesta");
        println("4. Pasar");
        println("5. Retirarse");
        println("Seleccione una opción:");
    }
    private void mostrarMenuPrincipal() {
        println("\n-- MENU PRINCIPAL --");
        println("1. Agregar jugador");
        println("2. Mostrar lista de jugadores");
        println("3. Comenzar partida");
        println("0. Salir");
        print("Seleccione una opcion: ");
        estadoActual = EstadoVistaConsola.MENU_PRINCIPAL;
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
