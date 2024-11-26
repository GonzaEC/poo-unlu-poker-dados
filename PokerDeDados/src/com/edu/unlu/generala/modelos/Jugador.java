package com.edu.unlu.generala.modelos;

public class Jugador extends Persona {
    private String nombre;
    private int saldo;
    private Apuesta apostado;

    public Jugador(String nombre, int saldoInicial) {
        this.nombre = nombre;
        //if (saldoInicial > 0) {}
        this.saldo = saldoInicial;
    }

    public int getSaldo() {
        return saldo;
    }

    public void agregarSaldo(int saldo){
        this.saldo += saldo;
    }
    public boolean retirarSaldo(int saldo) {
        if (this.saldo >= saldo) {
            this.saldo -= saldo;
            return true;
        }
        else{
            return false;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public int getApostado() {
        return apostado.getCantidad();
    }

    public void setApostado(Apuesta apostado) {
        this.apostado = apostado;
    }
}
