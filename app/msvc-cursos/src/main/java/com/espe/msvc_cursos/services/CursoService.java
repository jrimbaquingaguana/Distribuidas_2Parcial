package com.espe.msvc_cursos.services;

import com.espe.msvc_cursos.models.entity.Curso;
import com.espe.msvc_cursos.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();

    Optional<Curso> porId(Long id);

    Curso guardar(Curso curso);

    void eliminar(Long id);

    // Actualiza la documentación y firma de los métodos para reflejar la capacidad de manejar múltiples cursos por usuario
    Optional<Usuario> agregarUsuarioACurso(Usuario usuario, Long cursoId);

    Optional<Usuario> crearUsuarioYAsignarACurso(Usuario usuario, Long cursoId);

    boolean desasignarUsuarioDeCurso(Long usuarioId, Long cursoId);
}
