package com.edu.unlu.generala.modelos;

public class ManoPoker {
    private Vaso vaso;

    public int[] construirRecuento(int[] valores){
        int[] recuento = new int [6];

        for (int valor : valores){
            recuento[valor-1] ++;
        }
        return recuento;
    }

    public String verificarMano(int[] valores) {
        int[] recuento = construirRecuento(valores);

        if (esPokerReal(recuento)) return "Poker Real";
        if (esPokerCuadruple(recuento)) return "Poker Cuádruple";
        if (esFull(recuento)) return "Full";
        if (esEscaleraMayor(recuento)) return "Escalera Mayor";
        if (esEscaleraMenor(recuento)) return "Escalera Menor";
        if (esPierna(recuento)) return "Pierna";
        if (esParDoble(recuento)) return "Par Doble";
        if (esPar(recuento)) return "Par";

        return "Ninguna Mano";
    }

    private boolean esPar(int[] recuento) {
        for(int recuentos : recuento) {
            if (recuentos == 2){
                return true;
            }
        }
        return false;
    }

    private boolean esParDoble(int[] recuento) {
        int pares = 0;
        for(int recuentos : recuento) {
            if (recuentos == 2){
                pares++;
            }
        }
        return pares == 2;
    }

    private boolean esPierna(int[] recuento) {
        for(int recuentos : recuento) {
            if (recuentos == 3){
                return true;
            }
        }
        return false;
    }

    private boolean esEscaleraMenor(int[] recuento) {
        return recuento[0] == 1 && recuento[1] == 1 && recuento[2] == 1 && recuento[3] == 1 && recuento[4] == 1;
    }

    private boolean esEscaleraMayor(int[] recuento) {
        return recuento[1] == 1 && recuento[2] == 1 && recuento[3] == 1 && recuento[4] == 1 && recuento[5] == 1;
    }

    private boolean esFull(int[] recuento) {
        boolean resultado1 = false;
        boolean resultado2 = false;
        for(int recuentos : recuento) {
            if (recuentos == 3){
                resultado1 = true;
            }
            if(recuentos==2){
                resultado2 = true;
            }
        }
        return resultado1 && resultado2;
    }

    private boolean esPokerCuadruple(int[] recuento) {
        for(int recuentos : recuento) {
            if (recuentos == 4){
                return true;
            }
        }
        return false;
    }

    private boolean esPokerReal(int[] recuento) {
        for(int recuentos : recuento) {
            if (recuentos == 5){
                return true;
            }
        }
        return false;
    }
}
