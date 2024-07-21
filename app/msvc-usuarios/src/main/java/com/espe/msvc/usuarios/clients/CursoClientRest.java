package com.espe.msvc.usuarios.clients;

import com.espe.msvc.usuarios.models.entity.Usuario;
import com.espe.msvc.usuarios.models.Curso;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-cursos", url = "localhost:8002")
public interface CursoClientRest {

        @GetMapping("/cursos")
        ResponseEntity<List<Curso>> listar();

        @GetMapping("/cursos/{id}")
        ResponseEntity<Curso> detalle(@PathVariable Long id);

        @PostMapping("/cursos")
        ResponseEntity<Curso> crear(@RequestBody Curso curso);

        @PutMapping("/cursos/{id}")
        ResponseEntity<Curso> editar(@PathVariable Long id, @RequestBody Curso curso);

        @DeleteMapping("/cursos/{id}")
        ResponseEntity<Void> eliminar(@PathVariable Long id);

        @PutMapping("/cursos/crear-asignar-usuario/{idCurso}")
        ResponseEntity<?> crearYAsignarUsuario(@RequestBody Usuario usuario, @PathVariable Long idCurso);

        @DeleteMapping("/cursos/quitar-usuario/{idCurso}/{usuarioId}")
        ResponseEntity<?> quitarUsuario(@PathVariable Long idCurso, @PathVariable Long usuarioId);
}
