package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JButton aceptarButton;
    private ControladorGenerala controlador;

    public GUI(ControladorGenerala controlador){
        setTitle("Generala");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        this.controlador = controlador;
    }
}
