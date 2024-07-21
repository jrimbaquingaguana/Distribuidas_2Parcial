package com.espe.msvc_cursos.services;

import com.espe.msvc_cursos.models.entity.Curso;
import com.espe.msvc_cursos.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();

    Optional<Curso> porId (Long id);

    Curso guardar (Curso curso);

    void eliminar (Long id);

    Optional<Usuario> agregarUsuario (Usuario usuario, Long idCurso);

    Optional<Usuario> crearUsuario (Usuario usuario, Long idCurso);

    boolean eliminarUsuario(Long usuarioId, Long idCurso);
}
