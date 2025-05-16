package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Apuesta;

public interface IVista {
    void setControlador(ControladorGenerala controlador);
    void mostrarMensaje(String mensaje);
    void iniciar();


    void mostrarResultadoDados(int[] ints);

    void mostrarMenuJugadorDados();

    void mostrarMenuApuestas();
}
