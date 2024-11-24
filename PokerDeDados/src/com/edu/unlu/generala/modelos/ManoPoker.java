package com.edu.unlu.generala.modelos;
import java.util.HashMap;
import java.util.Map;

public class ManoPoker {
    private Vaso vaso;

    public int[] construirRecuento(int[] valores){
        int[] recuento = new int [6];

        for (int valor : valores){
            recuento[valor-1] ++;
        }
        return recuento;
    }

    public void verificarMano(int[] valores){
        int[] recuento = construirRecuento(valores);

        if(esPokerReal(recuento)) return;
        if(esPokerCuadruple(recuento)) return;
        if(esFull(recuento)) return;
        if(esEscaleraMayor(recuento)) return;
        if(esEscaleraMenor(recuento)) return;
        if(esPierna(recuento)) return;
        if(esParDoble(recuento)) return;
        if(esPar(recuento)) return;

    }

    private boolean esPar(int[] recuento) {
        
    }

    private boolean esParDoble(int[] recuento) {
    }

    private boolean esPierna(int[] recuento) {
    }

    private boolean esEscaleraMenor(int[] recuento) {
    }

    private boolean esEscaleraMayor(int[] recuento) {
    }

    private boolean esFull(int[] recuento) {
    }

    private boolean esPokerCuadruple(int[] recuento) {
    }

    private boolean esPokerReal(int[] recuento) {
    }
}
