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
    public List<Curso> Listar() {
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
        return ResponseEntity.status (HttpStatus.CREATED).body(service.guardar (curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Curso curso, @PathVariable Long id) {
        Optional<Curso> cursoOptional = service.porId(id);
        if (cursoOptional.isPresent()){
            Curso cursoDB = cursoOptional.get();
            cursoDB.setNombre (curso.getNombre());
            return ResponseEntity.status (HttpStatus.CREATED).body (service.guardar (cursoDB));
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id) {
        Optional<Curso> cursoOptional = service.porId(id);
        if (cursoOptional.isPresent()){
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{idCurso}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long idCurso){
        try {
            // Intenta agregar el usuario al curso
            Optional<Usuario> o = service.agregarUsuario(usuario, idCurso);
            if (o.isPresent()) {
                // Si el usuario se agrega correctamente, devuelve 201 Created
                return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
            } else {
                // Si el usuario o el curso no existen, devuelve 404 Not Found
                return ResponseEntity.notFound().build();
            }
        } catch (FeignException.NotFound e){
            // Si Feign no encuentra el recurso (usuario o curso), devuelve 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "Recurso no encontrado: " + e.getMessage()));
        } catch (Exception e) {
            // Para cualquier otro error, devuelve 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/quitar-usuario/{idCurso}/{usuarioId}")
    public ResponseEntity<?> quitarUsuario(@PathVariable Long idCurso, @PathVariable Long usuarioId){
        boolean result = service.eliminarUsuario(usuarioId, idCurso);
        if (result) {
            return ResponseEntity.ok().body(Collections.singletonMap("mensaje", "Usuario eliminado del curso con éxito"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "Recurso no encontrado: Usuario o Curso no encontrado"));
        }
    }
}
