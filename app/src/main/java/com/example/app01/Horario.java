package com.example.app01;

public class Horario {
    private String inicio;
    private String fin;

    public Horario(String inicio, String fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public String getInicio() {
        return inicio;
    }

    public String getFin() {
        return fin;
    }

    public String getFormattedTimeSlot() {
        return inicio + " - " + fin;
    }
}
