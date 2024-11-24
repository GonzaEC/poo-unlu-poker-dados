package com.edu.unlu.generala.modelos;

public class Jugador extends Persona {
    private String nombre;
    private float saldo;

    public Jugador(String nombre, float saldoInicial) {
        this.nombre = nombre;
        this.saldo = saldoInicial;
    }

    public float getSaldo() {
        return saldo;
    }

    public void agregarSaldo(float saldo){
        this.saldo += saldo;
    }
}
