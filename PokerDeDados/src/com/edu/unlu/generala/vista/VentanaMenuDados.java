package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class VentanaMenuDados extends JFrame {
    private ControladorGenerala controlador;
    private JLabel lblJugador;
    private JLabel lblTiradasRestantes;
    private JButton btnTirarTodos;
    private JButton btnApartarDados;
    private JButton btnPlantarse;

    private JPanel panelDados;
    private JToggleButton[] botonesDados;
    private static final int CANT_DADOS = 5;
    VentanaMenuApuestas ventanaApuestas;
    private static final Dimension TAM_BOTON = new Dimension(280, 50);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 20);
    private static final Color COLOR_FUENTE_LABEL = Color.WHITE;

    public VentanaMenuDados(ControladorGenerala controlador) {
        this.controlador = controlador;

        setTitle("Menú de Dados - Generala");
        setSize(800, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PanelFondo panel = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        lblJugador = new JLabel("Turno de: " + controlador.getJugadorActual().getNombre());
        lblJugador.setFont(FUENTE_LABEL);
        lblJugador.setForeground(COLOR_FUENTE_LABEL);
        lblJugador.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTiradasRestantes = new JLabel("Tiradas restantes: " + controlador.getTiradasRestantes());
        lblTiradasRestantes.setFont(new Font("Arial", Font.PLAIN, 18));
        lblTiradasRestantes.setForeground(COLOR_FUENTE_LABEL);
        lblTiradasRestantes.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel para los dados
        panelDados = new JPanel();
        panelDados.setOpaque(false);  // para que el fondo se vea
        panelDados.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelDados.setMaximumSize(new Dimension(800, 120));

        botonesDados = new JToggleButton[CANT_DADOS];
        for (int i = 0; i < CANT_DADOS; i++) {
            botonesDados[i] = new JToggleButton();
            botonesDados[i].setPreferredSize(new Dimension(80, 80));
            botonesDados[i].setContentAreaFilled(false);
            botonesDados[i].setFocusPainted(false);
            botonesDados[i].setBorderPainted(false);
            panelDados.add(botonesDados[i]);
        }

        btnTirarTodos = new BotonRedondeado("1. Tirar todos los dados", 50);
        estilizarBoton(btnTirarTodos);
        configurarBoton(btnTirarTodos);
        btnTirarTodos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTirarTodos.setMaximumSize(TAM_BOTON);

        btnApartarDados = new BotonRedondeado("2. Apartar dados y tirar restantes", 50);
        estilizarBoton(btnApartarDados);
        configurarBoton(btnApartarDados);
        btnApartarDados.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnApartarDados.setMaximumSize(TAM_BOTON);

        btnPlantarse = new BotonRedondeado("3. Plantarse (no tirar más)", 50);
        estilizarBoton(btnPlantarse);
        configurarBoton(btnPlantarse);
        btnPlantarse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPlantarse.setMaximumSize(TAM_BOTON);

        panel.add(lblJugador);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblTiradasRestantes);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));

        panel.add(panelDados);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));

        panel.add(btnTirarTodos);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnApartarDados);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnPlantarse);

        setContentPane(panel);

        actualizarBotones();
        inicializarDados();

        btnTirarTodos.addActionListener(e -> {
            try {
                controlador.tirarTodosDados();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

        });

        btnApartarDados.addActionListener(e -> {
            // Lista para los índices de dados a tirar (los que NO están apartados)
            java.util.List<Integer> indicesDadosATirar = new java.util.ArrayList<>();

            for (int i = 0; i < CANT_DADOS; i++) {
                if (!botonesDados[i].isSelected()) {
                    indicesDadosATirar.add(i);
                }
            }

            // Pasar la lista directamente al controlador
            controlador.tirarDadosSeleccionados(indicesDadosATirar);

        });

        btnPlantarse.addActionListener(e -> {
            try {
                controlador.plantarse();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            boolean etapaTerminada = controlador.todosPlantados();
            this.dispose(); // cerrar ventana actual

            if (etapaTerminada) {
                // Pasar a la etapa de apuestas, abrir ventana de apuestas
                VentanaMenuApuestas ventanaApuestas = null;
                try {
                    ventanaApuestas = new VentanaMenuApuestas(controlador);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                ventanaApuestas.setVisible(true);
            } else {
                // Seguir con el siguiente jugador en la etapa de dados
                VentanaMenuDados ventanaSiguienteJugador = new VentanaMenuDados(controlador);
                ventanaSiguienteJugador.setVisible(true);
            }

        });
    }

    private void inicializarDados() {
        // Al principio los dados pueden estar en 1 o en blanco
        int[] valoresIniciales = {1, 1, 1, 1, 1};
        actualizarDados(valoresIniciales);
    }

    public void actualizarDados(int[] valoresDados) {
        for (int i = 0; i < CANT_DADOS; i++) {
            botonesDados[i].setIcon(cargarIconoDado(valoresDados[i]));
            botonesDados[i].setSelected(false);
        }
    }
    private ImageIcon cargarIconoDado(int numero) {
        String path = "src/com/edu/unlu/generala/utils/dado" + numero + ".png";
        ImageIcon icono = new ImageIcon(path);
        Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    public void actualizarEstadoYVista() {
        lblTiradasRestantes.setText("Tiradas restantes: " + controlador.getTiradasRestantes());
        actualizarBotones();
        lblJugador.setText("Turno de: " + controlador.getJugadorActual().getNombre());
    }

    public void actualizarBotones() {
        int tiradas = controlador.getTiradasRestantes();
        btnTirarTodos.setEnabled(tiradas == 2);
        btnApartarDados.setEnabled(tiradas == 1);
    }



    private void estilizarBoton(JButton boton) {
        Color marronMedio = new Color(107, 76, 59);
        boton.setBackground(marronMedio);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
    }

    private void configurarBoton(JButton boton) {
        boton.setPreferredSize(new Dimension(200, 50));
        boton.setMaximumSize(new Dimension(200, 50));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}

