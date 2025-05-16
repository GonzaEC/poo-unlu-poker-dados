package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private void procesarEntrada(String entrada) throws RemoteException {
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
            case INGRESA_INDICES_RETIRO:
                try {
                    List<Integer> indices = Arrays.stream(entrada.trim().split("\\s+"))
                            .map(Integer::parseInt)
                            .filter(i -> i >= 0 && i < 5)
                            .collect(Collectors.toList());

                    controlador.tirarDadosSeleccionados(indices);
                    estadoActual = EstadoVistaConsola.MENU_JUGADOR_DADOS;
                    mostrarMenuJugadorDados();
                } catch (NumberFormatException e) {
                    println("Entrada inválida. Ingresá índices válidos del 0 al 4 separados por espacios:");
                }
                break;
            case MENU_JUGADOR_APUESTAS:
                procesarEntradaMenuApuestas(entrada);
                break;
            case SUBIR_APUESTA:
                
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

    private void procesarEntradaMenuPrincipal(String entrada) throws RemoteException {
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
        switch (entrada) {
            case "1":
                // Opción: Tirar todos los dados
                controlador.tirarTodosDados();  // Método que tira todos los dados (primera tirada)
                mostrarMenuJugadorDados();      // Volver a mostrar menú para la siguiente acción
                break;
            case "2":
                // Opción: Apartar dados y volver a tirar los restantes
                println("Ingresá los índices de los dados que querés volver a tirar (de 0 a 4, separados por espacios):");
                estadoActual = EstadoVistaConsola.INGRESA_INDICES_RETIRO;
                break;
            case "3":
                // Opción: Plantarse (terminar turno sin tirar más)
                controlador.plantarse();
                break;
            default:
                println("Opción inválida, intente de nuevo.");
                mostrarMenuJugadorDados();
                break;
        }
    }
    private void procesarEntradaMenuApuestas(String entrada) {
        switch (entrada){
            case "1":
                controlador.igualarApuesta();
                break;
            case "2":
                println("Cuanto queres apostar? ");
                estadoActual = EstadoVistaConsola.SUBIR_APUESTA;
                break;
            case "3":
                controlador.plantarseApuesta();
                break;
            default:
                println("Opción inválida, intente de nuevo.");
                mostrarMenuApuestas();
                break;
        }
    }
    public void mostrarMenuJugadorDados() {
        estadoActual = EstadoVistaConsola.MENU_JUGADOR_DADOS;
        println("\n-- TURNO DE: " + controlador.getJugadorActual().getNombre() + " --");
        println("Tiradas restantes: " + controlador.getTiradasRestantes());
        int tiradas = controlador.getTiradasRestantes();
        println("== MENÚ DE JUGADAS ==");
        if (tiradas == 2){
            println("1. Tirar todos los dados");
        }
        else if (tiradas == 1) {
            println("2. Apartar dados y volver a tirar los restantes");
        }
        println("3. Plantarse (no tirar más)");
        println("Seleccione una opción:");
    }
    public void mostrarMenuApuestas() {
        estadoActual = EstadoVistaConsola.MENU_JUGADOR_APUESTAS;
        Jugador jugador = controlador.getJugadorActual();

        println("\n-- RONDA DE APUESTAS --");
        println("Turno de: " + jugador.getNombre());
        println("Pozo actual: $" + controlador.getPozo());
        println("Tu apuesta actual: $" + jugador.getApostado());
        println("Apuesta máxima actual: $" + controlador.getApuestaMaxima());

        println("¿Qué deseás hacer?");
        println("1. Igualar (Call)");
        println("2. Subir (Raise)");
        println("3. Plantarse (Fold)");
        print("Elegí una opción: ");
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
