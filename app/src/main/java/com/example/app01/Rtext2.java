package com.example.app01;

public class Rtext2 {
    private String name;
    private String cantidad;
    private String intervalo;
    private String desc;
    private String fecha;

    // Constructor vacío necesario para Firestore
    public Rtext2() {}

    // Constructor con parámetros
    public Rtext2(String name, String cantidad, String intervalo, String desc, String fecha) {
        this.name = name;
        this.cantidad = cantidad;
        this.intervalo = intervalo;
        this.desc = desc;
        this.fecha = fecha;
    }

    // Getters (siguiendo la convención de nombres para serialización)
    public String getName() { return name; }
    public String getCantidad() { return cantidad; }
    public String getIntervalo() { return intervalo; }
    public String getDesc() { return desc; }
    public String getFecha() { return fecha; }

    // Setters (opcionales, pero recomendados para permitir que Firestore actualice los datos)
    public void setName(String name) { this.name = name; }
    public void setCantidad(String cantidad) { this.cantidad = cantidad; }
    public void setIntervalo(String intervalo) { this.intervalo = intervalo; }
    public void setDesc(String desc) { this.desc = desc; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
