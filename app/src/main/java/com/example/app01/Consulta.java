package com.example.app01;

public class Consulta {
    private String nombreMascota;
    private String fecha;
    private String veterinario;
    private String diagnostico;
    private String tratamiento;


    public Consulta(String nombreMascota, String fecha) {
        this.nombreMascota = nombreMascota;
        this.fecha = fecha;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
