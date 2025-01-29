package com.edu.unlu.generala.modelos;

import java.io.Serializable;

public enum Eventos implements Serializable {
    INICIAR_JUEGO,
    REGISTRAR_JUGADORES,
    REALIZAR_APUESTAS,
    TIRAR_DADOS,
    SEGUNDO_TIRO,
    CAMBIAR_TURNO,
    FIN_JUEGO,
    NUEVA_RONDA
}

