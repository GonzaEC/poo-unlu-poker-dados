package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Dado;
import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


public class InterfazGrafica extends JFrame implements IVista {
    private JButton btnAgregarJugador;
    private JButton btnVerJugadores;
    private JButton btnIniciarJuego;
    private JButton btnSalir;
    private JPanel contenedorPrincipal;
    private CardLayout cardLayout;
    private ControladorGenerala controlador;
    private JTextArea areaJugadores;
    private String nombreJugador;

    public InterfazGrafica(ControladorGenerala controlador) {
        setTitle("Juego de Dados");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana
        this.controlador = controlador;
        inicializarComponentes();
    }
    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);


        JPanel panelMenu = crearPanelMenu();
        JPanel panelAgregarJugador = crearPanelAgregarJugador();

        contenedorPrincipal.add(panelMenu, "menu");
        contenedorPrincipal.add(panelAgregarJugador, "agregarJugador");

        JPanel panelVerJugadores = crearPanelVerJugadores();


        contenedorPrincipal.add(panelVerJugadores, "verJugadores");

        setContentPane(contenedorPrincipal);
    }

    private JPanel crearPanelMenu() {
        PanelFondo panelFondo = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panelFondo.setLayout(new GridBagLayout());

        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        btnAgregarJugador = new BotonRedondeado("Agregar Jugador", 50);
        estilizarBoton(btnAgregarJugador);
        configurarBoton(btnAgregarJugador);
        btnAgregarJugador.addActionListener(e -> cardLayout.show(contenedorPrincipal, "agregarJugador"));

        btnVerJugadores = new BotonRedondeado("Ver Jugadores", 50);
        estilizarBoton(btnVerJugadores);
        configurarBoton(btnVerJugadores);
        btnVerJugadores.addActionListener(e -> {
            actualizarAreaJugadores();
            cardLayout.show(contenedorPrincipal, "verJugadores");
        });

        btnIniciarJuego = new BotonRedondeado("Iniciar Juego", 50);
        estilizarBoton(btnIniciarJuego);
        configurarBoton(btnIniciarJuego);
        btnIniciarJuego.addActionListener(e -> iniciarPartidaDirecto());

        btnSalir = new BotonRedondeado("Salir", 50);
        estilizarBoton(btnSalir);
        configurarBoton(btnSalir);
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnAgregarJugador);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 25)));
        panelBotones.add(btnVerJugadores);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 25)));
        panelBotones.add(btnIniciarJuego);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 25)));
        panelBotones.add(btnSalir);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFondo.add(panelBotones, gbc);

        return panelFondo;
    }

    private JPanel crearPanelAgregarJugador() {
        PanelFondo panel = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JLabel lbl = new JLabel("Nombre del jugador:");
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtNombre = new JTextField();
        txtNombre.setMaximumSize(new Dimension(300, 30));
        txtNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnConfirmar = new BotonRedondeado("Confirmar", 50);
        estilizarBoton(btnConfirmar);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnVolver = new BotonRedondeado("Volver", 50);
        estilizarBoton(btnVolver);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnConfirmar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (!nombre.isEmpty()) {
                agregarJugador(nombre); // Acá podrías pasarlo al modelo
                txtNombre.setText("");
                cardLayout.show(contenedorPrincipal, "menu");
            } else {
                JOptionPane.showMessageDialog(this, "Por favor ingresá un nombre.");
            }
        });

        btnVolver.addActionListener(e -> cardLayout.show(contenedorPrincipal, "menu"));

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(txtNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(btnConfirmar);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnVolver);

        return panel;
    }
    private void agregarJugador(String nombreJugador) {
        if (!nombreJugador.isEmpty()) {
            if (controlador.existeJugador(nombreJugador)) {
                mostrarMensaje("El nombre del jugador ya existe. Por favor, ingrese un nombre diferente.");
            } else {
                controlador.agregarJugador(nombreJugador);
                mostrarMensaje("Jugador " + nombreJugador + " agregado.");
                this.nombreJugador = nombreJugador;

            }
        } else {
            mostrarMensaje("Por favor, ingrese un nombre válido.");
        }
    }

    private JPanel crearPanelVerJugadores() {
        PanelFondo panel = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JLabel lbl = new JLabel("Lista de jugadores:");
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        areaJugadores = new JTextArea();
        areaJugadores.setEditable(false);
        areaJugadores.setMaximumSize(new Dimension(400, 300));
        areaJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnVolver = new BotonRedondeado("Volver", 50);
        estilizarBoton(btnVolver);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.addActionListener(e -> cardLayout.show(contenedorPrincipal, "menu"));

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(areaJugadores);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnVolver);

        return panel;
    }
    private void actualizarAreaJugadores() {
        areaJugadores.setText(""); // limpia primero
        List<Jugador> jugadores = controlador.getJugadores(); // suponiendo que tenés acceso al controlador
        for (Jugador j : jugadores) {
            areaJugadores.append("- " + j.getNombre() + "\n"); // o más info si querés
        }
    }


    private void iniciarPartidaDirecto() {
        if (controlador.cantidadJugadores() < 2) {
            JOptionPane.showMessageDialog(this, "Necesitás que haya dos jugadores para iniciar la partida.");
            return;  // ¡Importante! Salir para que no abra la ventana
        }

        // Acá continúa normalmente si hay 2 o más jugadores
        VentanaMenuDados ventanaDados = new VentanaMenuDados(controlador);
        ventanaDados.setVisible(true);

        // También podrías ocultar o cerrar esta ventana si corresponde
        this.dispose();
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

    @Override
    public void setControlador(ControladorGenerala controlador) {
        this.controlador= controlador;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        var ventanaMensaje = new Mensaje (mensaje);
        ventanaMensaje.setVisible(true);
    }

    public void iniciar() {
        setVisible(true);
    }

    @Override
    public void mostrarMenuJugadorDados() {

    }

    @Override
    public void mostrarMenuApuestas() {

    }

    public void mostrarGanador() {
        JOptionPane.showMessageDialog(this, "Ganador: " + controlador.determinarGanador().getNombre(), "Ganador", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarTirada(List<Dado> dadosTirados) {

    }
    public void notificarMensaje(String string) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, string);
        });
    }
}
