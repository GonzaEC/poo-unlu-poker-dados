package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;

public interface IVista {
    void setControlador(ControladorGenerala controlador);
    void mostrarMensaje(String mensaje);
    void iniciar();
}
