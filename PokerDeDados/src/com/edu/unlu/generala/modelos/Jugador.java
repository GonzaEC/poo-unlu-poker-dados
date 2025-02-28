package com.edu.unlu.generala.modelos;

public class Jugador extends Persona {
    private String nombre;
    private int saldo;
    private Apuesta apostado;
    private Vaso vasoJugador;
    private ManoPoker manoPoker;
    private boolean haApostado = false;

    public Jugador(String nombre, int saldoInicial) {
        this.nombre = nombre;
        //if (saldoInicial > 0) {}
        this.saldo = saldoInicial;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getApostado() {
        return apostado.getCantidad();
    }

    public Vaso getVasoJugador(){return this.vasoJugador;}

    public int getManoPoker(){
        int[] valoresDados = vasoJugador.obtenerValores();
        return manoPoker.verificarMano(valoresDados);
    }

    public ManoPoker getMano(){
        return this.manoPoker;
    }

    public void setApostado(Apuesta apostado) {
        this.apostado = apostado;
    }

    public void agregarSaldo(int saldo){
        this.saldo += saldo;
    }

    public boolean retirarSaldo(int monto) {
        if(saldo > monto){
            saldo -= monto;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean haApostado() {
        return haApostado;
    }
    public void setHaApostado(boolean haApostado) {
        this.haApostado = haApostado;
    }
}
