package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Apuesta;
import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

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
        println("\n-- MENU PRINCIPAL --");
        println("1. Iniciar nueva partida");
        println("2. Ver historial de partidas");
        println("3. Salir");
        print("Seleccione una opcion: ");
    }

    private void procesarEntrada(String entrada){
        switch (estadoActual){
            case MENU_PRINCIPAL:
                procesarEntradaMenuPrincipal(entrada);
                break;
            case ALTA_JUGADOR:
                registrarJugador(entrada);
                break;
            case RONDA_APUESTAS_INICIAL:
                procesarApuesta(entrada);
                break;
            case TIRAR_DADOS:
                tirarDados(entrada);
                break;
            case MANTENER_DADOS:
                mantenerDados(entrada);
                break;
            case RONDA_APUESTAS_FINAL:
                procesarApuestaFinal(entrada);
                break;
            default:
                println("Estado invalido. Reiniciando...");
                estadoActual = EstadoVistaConsola.MENU_PRINCIPAL;
                mostrarMenuPrincipal();
                break;

        }
    }

    private void procesarApuestaFinal(String entrada) {
        try {
            int monto = Integer.parseInt(entrada); // Convertir la entrada a un número
            Jugador jugadorActual = controlador.getJugadorActual();

            if (monto <= 0) {
                println("El monto de la apuesta debe ser mayor que 0.");
            } else if (monto > jugadorActual.getSaldo()) {
                println("Saldo insuficiente para realizar la apuesta.");
            } else {
                if (controlador.realizarApuesta(jugadorActual, monto)) {
                    println("Apuesta final realizada: $" + monto);
                    evaluarJugada(); // Evaluar la jugada y determinar el ganador
                } else {
                    println("Error al realizar la apuesta final.");
                }
            }
        } catch (NumberFormatException e) {
            println("Entrada inválida. Ingrese un número entero.");
        } catch (RemoteException e) {
            println("Error al realizar la apuesta final: " + e.getMessage());
        }
    }

    private void procesarApuesta(String entrada) {
        try {
            int monto = Integer.parseInt(entrada); // Convertir la entrada a un número
            Jugador jugadorActual = controlador.getJugadorActual();

            if (monto <= 0) {
                println("El monto de la apuesta debe ser mayor que 0.");
            } else if (monto > jugadorActual.getSaldo()) {
                println("Saldo insuficiente para realizar la apuesta.");
            } else {
                if (controlador.realizarApuesta(jugadorActual, monto)) {
                    println("Apuesta realizada: $" + monto);
                    estadoActual = EstadoVistaConsola.TIRAR_DADOS; // Cambiar al estado de tirar dados
                    println("Es el turno de: " + jugadorActual.getNombre());
                } else {
                    println("Error al realizar la apuesta.");
                }
            }
        } catch (NumberFormatException e) {
            println("Entrada inválida. Ingrese un número entero.");
        }
    }

    private void mantenerDados(String entrada) {
        try {
            String[] indicesStr = entrada.split(",");
            ArrayList<Integer> indices = new ArrayList<>();

            for (String indice : indicesStr) {
                indices.add(Integer.parseInt(indice.trim()) - 1);
            }

            controlador.tirarDadosSeleccion(indices);

            mostrarResultadoDados(controlador.getJugadorActual().getVasoJugador().obtenerValores());

            if (controlador.getTiradasRestantes() == 0) {
                evaluarJugada();
            } else {
                estadoActual = EstadoVistaConsola.TIRAR_DADOS;
            }
        } catch (NumberFormatException e) {
            println("Entrada inválida. Ingrese números separados por comas (ejemplo: 1,3,5).");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    private void tirarDados(String entrada) {
        try {
            Jugador jugadorActual = controlador.getJugadorActual();

            jugadorActual.getVasoJugador().lanzarDados();
            mostrarResultadoDados(jugadorActual.getVasoJugador().obtenerValores());
            if (controlador.getTiradasRestantes() > 0) {
                mostrarMenuJugador();
            } else {
                try {
                    evaluarJugada();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void mostrarResultadoDados(int[] valoresDados) {
        println("Resultado de los dados:");
        for (int i = 0; i < valoresDados.length; i++) {
            println("Dado " + (i + 1) + ": " + valoresDados[i]);
        }
    }
    private void realizarApuesta(String entrada) {
        try {
            // Convertir la entrada a un número entero (monto de la apuesta)
            int monto = Integer.parseInt(entrada);

            // Obtener el jugador actual
            Jugador jugadorActual = controlador.getJugadorActual();

            // Validar que el monto sea válido
            if (monto <= 0) {
                println("El monto de la apuesta debe ser mayor que 0.");
            } else if (monto > jugadorActual.getSaldo()) {
                println("Saldo insuficiente para realizar la apuesta.");
            } else {
                // Realizar la apuesta a través del controlador
                if (controlador.realizarApuesta(jugadorActual, monto)) {
                    println("Apuesta realizada: $" + monto);
                    estadoActual = EstadoVistaConsola.TIRAR_DADOS; // Cambiar al estado de tirar dados
                    println(jugadorActual.getNombre()); // Mostrar el turno del jugador
                } else {
                    println("Error al realizar la apuesta.");
                }
            }
        } catch (NumberFormatException e) {
            println("Entrada invalida, ingrese un numero entero.");
        }
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
                    estadoActual = EstadoVistaConsola.ALTA_APUESTA;
                    println(controlador.jugadorActual());
                    println("Ingrese el monto de la apuesta: ");
                }
                break;
            case "3": // Salir
                System.exit(0);
                break;
            default:
                println("Opcion invalida, intente nuevamente.");
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
    private void mostrarMenuJugador() {
        estadoActual = EstadoVistaConsola.MENU_JUGADOR;
        println("\n-- MENÚ DEL JUGADOR --");
        println("1. Apostar");
        println("2. Tirar dados");
        println("3. Mantener dados");
        println("4. Plantarse");
        print("Seleccione una opcion: ");
    }

    private void procesarEntradaMenuJugador(String entrada) throws RemoteException {
        switch (entrada){
            case "1":
                println("Ingrese monto a apostar");
                estadoActual = EstadoVistaConsola.ALTA_APUESTA;
                break;
            case "2":
                try{
                    controlador.tirarDados();
                    mostrarResultadoDados(controlador.getJugadorActual().getVasoJugador().obtenerValores());
                    if(controlador.getTiradasRestantes() > 0){
                        println("Desea mantener algunos dados? (1. SI/ 2. NO):");
                        estadoActual = EstadoVistaConsola.MANTENER_DADOS;
                    }
                    else {
                        evaluarJugada();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "3":
                println("Ingrese los indices de los dados a mantener (separados por comas): ");
                estadoActual = EstadoVistaConsola.MANTENER_DADOS;
                break;
            case "4":
                evaluarJugada();
                break;
            default:
                println("Opcion invalida, intente nuevamente.");
                mostrarMenuJugador();
                break;
        }
    }

    private void evaluarJugada() throws RemoteException {
        String jugada = controlador.evaluarJugada();
        println("Jugada: " + jugada);

        Jugador ganador = controlador.determinarGanador();
        if(ganador != null){
            println("El ganador es: " + ganador.getNombre());

        }else{
            println("No hay ganador en esta ronda");
        }

        controlador.reiniciarPartida();
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
