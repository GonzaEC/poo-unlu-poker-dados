package com.edu.unlu.generala.modelos;

import java.util.Random;

public class Dado {
    private int cara;
    static private Random numeroAleatorio;

    public Dado(){
        if(numeroAleatorio == null){
            numeroAleatorio = new Random();
        }
        tirar();
    }

    public void tirar() {
        cara = numeroAleatorio.nextInt(6)+1;
    }

    public int getCara(){
        return this.cara;
    }

}
