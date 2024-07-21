package com.espe.msvc_cursos.models;

import com.espe.msvc_cursos.models.entity.CursoUsuario;
import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private String password;
    private List<CursoUsuario> cursos = new ArrayList<>();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CursoUsuario> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoUsuario> cursos) {
        this.cursos = cursos;
    }

    public void addCurso(CursoUsuario cursoUsuario) {
        this.cursos.add(cursoUsuario);
    }

    public void removeCurso(CursoUsuario cursoUsuario) {
        this.cursos.remove(cursoUsuario);
    }
}
