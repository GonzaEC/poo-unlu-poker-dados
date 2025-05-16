package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Apuesta;
import com.edu.unlu.generala.modelos.Dado;

import java.util.List;

public interface IVista {
    void setControlador(ControladorGenerala controlador);
    void mostrarMensaje(String mensaje);
    void iniciar();

    void mostrarMenuJugadorDados();

    void mostrarMenuApuestas();

    void mostrarGanador();

    void  mostrarTirada(List<Dado> dadosTirados);
}
