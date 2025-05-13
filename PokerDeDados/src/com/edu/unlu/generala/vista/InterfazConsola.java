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
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
        txtEntrada.setText("");
    }
    private void procesarEntrada(String entrada){
        switch (estadoActual){
            case MENU_PRINCIPAL:
                limpiarPantalla():
                procesarEntradaMenuPrincipal(entrada);
                break;
            case AGREGAR_JUGADOR:
                agregarJugador();

        }
    }

    private void agregarJugador() {
        if(!txtEntrada.getText().equals("")) {
            nombreJugador= txtEntrada.getText();
            if(!controlador.existeJugador(nombreJugador)){
                controlador.agregarJugador(nombreJugador);
                println("   Jugador agregado con exito");
                mostrarMenuPrincipal();
            }
            else{
                println(" Ese jugador ya se encuentra registrado, ingrese otro : ");
            }
        }
        else{
            println("   Igrese un nombre valido: ");
        }

    }

    private void procesarEntradaMenuPrincipal(String entrada) {
        switch (entrada) {
            case "1":
                //agregar jugador
                println("Nombre del jugador: ");
                estadoActual = EstadoVistaConsola.AGREGAR_JUGADOR;
                break;
        }
    }

    private void menuJugador() {
        estadoActual = EstadoVistaConsola.MENU_JUGADOR;
        println("\n-- TURNO DE: " + controlador.getJugadorActual().getNombre() + " --");
        println("1. Apostar");
        println("2. Tirar dados");
        println("3. Mantener dados");
        println("4. Plantarse");
        print("Seleccione una opcion: ");
    }
    private void mostrarMenuPrincipal() {
        println("\n-- MENU PRINCIPAL --");
        println("1. Agregar jugador");
        println("2. Mostrar lista de jugadores");
        println("3. Comenzar partida");
        println("4. Salir");
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
