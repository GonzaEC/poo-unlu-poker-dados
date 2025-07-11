package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class VentanaMenuApuestas extends JFrame {
    private ControladorGenerala controlador;

    private JLabel lblJugador;
    private JLabel lblPozo;
    private JLabel lblApuestaJugador;
    private JLabel lblApuestaMaxima;
    private JLabel lblInstruccion;
    private JTextField txtApuesta;
    private JButton btnConfirmar;
    private JButton btnCancelar;

    private JButton btnIgualar;
    private JButton btnSubir;
    private JButton btnPlantarse;

    private static final Dimension TAM_BOTON = new Dimension(280, 50);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 20);
    private static final Color COLOR_FUENTE_LABEL = Color.WHITE;

    public VentanaMenuApuestas(ControladorGenerala controlador) throws RemoteException {
        this.controlador = controlador;

        setTitle("Menú de Apuestas - Generala");
        setSize(800, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PanelFondo panel = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        panel.setOpaque(false);


        lblJugador = new JLabel("Turno de: " + controlador.getJugadorActual().getNombre());
        lblJugador.setFont(FUENTE_LABEL);
        lblJugador.setForeground(COLOR_FUENTE_LABEL);
        lblJugador.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblPozo = new JLabel("Pozo actual: $" + controlador.getPozo());
        lblPozo.setFont(FUENTE_LABEL);
        lblPozo.setForeground(COLOR_FUENTE_LABEL);
        lblPozo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblApuestaJugador = new JLabel("Tu apuesta actual: $" + controlador.getJugadorActual().getApostado());
        lblApuestaJugador.setFont(FUENTE_LABEL);
        lblApuestaJugador.setForeground(COLOR_FUENTE_LABEL);
        lblApuestaJugador.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblApuestaMaxima = new JLabel("Apuesta máxima actual: $" + controlador.getApuestaMaxima());
        lblApuestaMaxima.setFont(FUENTE_LABEL);
        lblApuestaMaxima.setForeground(COLOR_FUENTE_LABEL);
        lblApuestaMaxima.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnIgualar = new BotonRedondeado("1. Igualar (Call)",50);
        estilizarBoton(btnIgualar);
        configurarBoton(btnIgualar);
        btnSubir = new BotonRedondeado("2. Subir (Raise)",50);
        estilizarBoton(btnSubir);
        configurarBoton(btnSubir);
        btnPlantarse = new BotonRedondeado("3. Plantarse (Fold)",50);
        estilizarBoton(btnPlantarse);
        configurarBoton(btnPlantarse);

        btnIgualar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSubir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPlantarse.setAlignmentX(Component.CENTER_ALIGNMENT);



        panel.add(lblJugador);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblPozo);
        panel.add(lblApuestaJugador);
        panel.add(lblApuestaMaxima);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(btnIgualar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnSubir);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnPlantarse);

        add(panel);

        // Acciones botones
        btnIgualar.addActionListener(e -> {
            try {
                controlador.igualarApuesta();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnSubir.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "¿Cuánto querés apostar?");
            try {
                controlador.subirApuesta(input);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida, intentá de nuevo.");
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnPlantarse.addActionListener(e -> {
            try {
                controlador.plantarseApuesta();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    // Método para actualizar la vista cuando se vuelve a mostrar
    public void actualizarDatos() throws RemoteException {
        lblJugador.setText("Turno de: " + controlador.getJugadorActual().getNombre());
        lblPozo.setText("Pozo actual: $" + controlador.getPozo());
        lblApuestaJugador.setText("Tu apuesta actual: $" + controlador.getJugadorActual().getApostado());
        lblApuestaMaxima.setText("Apuesta máxima actual: $" + controlador.getApuestaMaxima());
    }

    // Método para mostrar la ventana (y actualizar datos)
    public void mostrar() throws RemoteException {
        actualizarDatos();
        setVisible(true);
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
