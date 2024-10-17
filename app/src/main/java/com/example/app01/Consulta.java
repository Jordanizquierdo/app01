package com.example.app01;

public class Consulta {
    private String veterinario;
    private String fecha; // Esta puede seguir siendo la fecha de la consulta
    private String documentoId; // Nuevo campo para almacenar el ID del documento

    // Constructor vacío (necesario para Firestore)
    public Consulta() {
    }

    // Getters y setters
    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(String documentoId) {
        this.documentoId = documentoId; // Set para el ID del documento
    }
}
