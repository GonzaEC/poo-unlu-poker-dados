package com.edu.unlu.generala.modelos;

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

    public int[] obtenerValores() {
        int[] valores = new int[dados.length];
        for (int i = 0; i < dados.length; i++) {
            valores[i] = dados[i].getCara();
        }
        return valores;
    }




}
