package com.example.app01;

public class Rtext {
    private String nombre;
    private String cantidad;
    private String intervalo;
    private String descripcion;
    private String mascota;  // Mascota asociada al recordatorio

    // Constructor con los parámetros del recordatorio y la mascota
    public Rtext(String nombre, String cantidad, String intervalo, String descripcion, String mascota) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.intervalo = intervalo;
        this.descripcion = descripcion;
        this.mascota = mascota;  // Asignamos la mascota seleccionada
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getCantidad() { return cantidad; }
    public String getIntervalo() { return intervalo; }
    public String getDescripcion() { return descripcion; }
    public String getMascota() { return mascota; }

    // Setters (opcional)
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCantidad(String cantidad) { this.cantidad = cantidad; }
    public void setIntervalo(String intervalo) { this.intervalo = intervalo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setMascota(String mascota) { this.mascota = mascota; }
}
