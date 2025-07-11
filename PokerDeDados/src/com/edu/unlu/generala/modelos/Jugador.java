package com.edu.unlu.generala.modelos;

import java.io.Serializable;
import java.util.List;

public class Jugador extends Persona implements Serializable {
    private String nombre;
    private int saldo;
    private Apuesta apostado;
    private Vaso vasoJugador;
    private ManoPoker manoPoker;
    private boolean haApostado = false;
    private boolean plantado = false;
    List<Dado> dadosGuardados;
    private boolean sePlantoApuesta = false;

    public Jugador(String nombre, int saldoInicial) {
        this.nombre = nombre;
        //if (saldoInicial > 0) {}

        this.vasoJugador =new Vaso();
        this.saldo = saldoInicial;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getApostado() {
        return apostado != null ? apostado.getCantidad() : 0;
    }

    public Vaso getVasoJugador(){return this.vasoJugador;}

    public int getManoPoker(){
        int[] valoresDados = vasoJugador.getValores();
        return manoPoker.verificarMano(valoresDados);
    }

    public ManoPoker getMano(){
        return this.manoPoker;
    }
    public void setManoPoker(Vaso vasoNuevo){
        this.manoPoker = new ManoPoker(vasoNuevo);
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
    public void apostar(int monto){
        this.apostado = new Apuesta(this, monto);
        this.haApostado = true;
    }
    public Apuesta getApuesta(){
        return this.apostado;
    }
    public boolean haApostado() {
        return haApostado;
    }
    public void setHaApostado(boolean haApostado) {
        this.haApostado = haApostado;
    }


    public List<Dado> getDadosGuardados() {
        return dadosGuardados;
    }
    public void setDadosGuardados(List<Dado> dadosGuardados) {
        this.dadosGuardados = dadosGuardados;
    }

    public boolean isPlantado() {
        return this.plantado;
    }

    public void setPlantado(boolean b) {
        this.plantado = b;
    }

    public void aumentarApuesta(int cantidad) {
        // Verificás que tenga saldo suficiente
        if (saldo >= cantidad) {
            // Le restás al saldo la cantidad apostada
            saldo -= cantidad;

            // Sumás la cantidad a la apuesta actual
            if (apostado == null) {
                apostado = new Apuesta(this, cantidad);
            } else {
                // Sumás a la apuesta ya existente
                apostado.setApuesta(apostado.getCantidad() + cantidad);
            }

            // Indicás que ha apostado algo
            haApostado = true;
        } else {
            // Si no tiene saldo suficiente, podés manejar un error o excepción
            throw new IllegalArgumentException("Saldo insuficiente para aumentar apuesta");
        }
    }
    public boolean isPlantoApuesta() {
        return sePlantoApuesta;
    }

    public void setPlantoApuesta(boolean b) {
        this.sePlantoApuesta = b;
    }

}
