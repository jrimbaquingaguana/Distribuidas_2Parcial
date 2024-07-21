package com.espe.msvc_cursos.controllers;

import com.espe.msvc_cursos.models.Usuario;
import com.espe.msvc_cursos.models.entity.Curso;
import com.espe.msvc_cursos.services.CursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class CursoController {
    @Autowired
    private CursoService service;

    @GetMapping
    public List<Curso> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> cursoOptional = service.porId(id);
        if (cursoOptional.isPresent()) {
            return ResponseEntity.ok().body(cursoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Curso curso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Curso curso, @PathVariable Long id) {
        Optional<Curso> cursoOptional = service.porId(id);
        if (cursoOptional.isPresent()) {
            Curso cursoDB = cursoOptional.get();
            cursoDB.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> cursoOptional = service.porId(id);
        if (cursoOptional.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/asignar-usuario/{idCurso}")
    public ResponseEntity<?> agregarUsuarioACurso(@RequestBody Usuario usuario, @PathVariable Long idCurso) {
        try {
            Optional<Usuario> usuarioAgregado = service.agregarUsuarioACurso(usuario, idCurso);
            if (usuarioAgregado.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAgregado.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("mensaje", "Curso no encontrado o error al agregar el usuario"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @PutMapping("/crear-asignar-usuario/{idCurso}")
    public ResponseEntity<?> crearYAsignarUsuario(@RequestBody Usuario usuario, @PathVariable Long idCurso) {
        try {
            Optional<Usuario> usuarioCreado = service.crearUsuarioYAsignarACurso(usuario, idCurso);
            if (usuarioCreado.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "Recurso no encontrado: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/quitar-usuario/{idCurso}/{usuarioId}")
    public ResponseEntity<?> quitarUsuario(@PathVariable Long idCurso, @PathVariable Long usuarioId) {
        boolean result = service.desasignarUsuarioDeCurso(usuarioId, idCurso);
        if (result) {
            return ResponseEntity.ok().body(Collections.singletonMap("mensaje", "Usuario eliminado del curso con éxito"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "Recurso no encontrado: Usuario o Curso no encontrado"));
        }
    }
}
