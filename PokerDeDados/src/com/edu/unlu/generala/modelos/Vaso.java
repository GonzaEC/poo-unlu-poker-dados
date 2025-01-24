package com.edu.unlu.generala.modelos;

import java.util.ArrayList;

public class Vaso {
    private Dado dados[];

    //metodos
    public void lanzarDados(){
        for (Dado dado : dados) {
            dado.tirar();
        }
    }

    public Dado[] getDados() {
        return dados;
    }
    public int verDado(int dado){
        if (dado > 0 && dado <= dados.length){
            return dados[dado-1].getCara();
        }
        else{
            return -1;
        }
    }

    public int[] obtenerValores() {
        int[] valores = new int[dados.length];
        for (int i = 0; i < dados.length; i++) {
            valores[i] = dados[i].getCara();
        }
        return valores;
    }


    public void lanzarSeleccionados(ArrayList<Integer> indices) {
        for (Integer indice : indices) {
            if (indice >= 0 && indice < dados.length) {
                dados[indices.get(indice)].tirar();
            }
        }
    }

}
