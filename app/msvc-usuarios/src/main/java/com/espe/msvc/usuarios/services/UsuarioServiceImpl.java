package com.espe.msvc.usuarios.services;

import com.espe.msvc.usuarios.clients.CursoClientRest;
import com.espe.msvc.usuarios.models.entity.Usuario;
import com.espe.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoClientRest cursoClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario inscribirACurso(Long usuarioId, Long cursoId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isPresent()) {
            ResponseEntity<?> response = cursoClientRest.crearYAsignarUsuario(new Usuario(), cursoId);
            if (response.getStatusCode().is2xxSuccessful()) {
                return usuarioOptional.get();
            }
        }
        return null; // Consider handling with a custom exception
    }

    @Override
    @Transactional
    public Usuario desinscribirDeCurso(Long usuarioId, Long cursoId) {
        ResponseEntity<?> response = cursoClientRest.quitarUsuario(cursoId, usuarioId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return usuarioRepository.findById(usuarioId).orElse(null);
        }
        return null; // Consider handling with a custom exception
    }
}
