package com.espe.msvc_cursos.clients;

import com.espe.msvc_cursos.models.Usuario;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "msvc-usuarios", url = "localhost:8001")
public interface UsuarioClientRest {

    @GetMapping("/usuarios/buscar/{id}")
    Usuario detalle (@PathVariable Long id);

    @PostMapping("/usuarios/agregar")
    Usuario crear (@RequestBody Usuario usuario);

    @PutMapping("/usuarios/editar/{id}")
    Usuario editar(@PathVariable Long id, @RequestBody Usuario usuario);
}
