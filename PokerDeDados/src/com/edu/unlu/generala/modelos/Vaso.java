package com.edu.unlu.generala.modelos;

public class Vaso {
    private Dado dados[];

    //metodos
    public void lanzarDados(){
        for (int i = 0; i < dados.length; i++) {
            dados[i].tirar();
        }
    }

}
