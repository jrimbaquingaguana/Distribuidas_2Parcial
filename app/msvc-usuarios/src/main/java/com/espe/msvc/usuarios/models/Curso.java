package com.espe.msvc.usuarios.models;

import java.util.List;

public class Curso {

    private Long id;
    private String nombre;
    // Considera incluir otros campos relevantes que necesites del objeto Curso

    // Constructores, getters y setters

    public Curso() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
