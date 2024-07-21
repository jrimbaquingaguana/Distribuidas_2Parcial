package com.espe.msvc_cursos.services;

import com.espe.msvc_cursos.models.Usuario;
import com.espe.msvc_cursos.clients.UsuarioClientRest;
import com.espe.msvc_cursos.models.entity.CursoUsuario;
import com.espe.msvc_cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.espe.msvc_cursos.models.entity.Curso;
import java.util.List;
import java.util.Optional;
import feign.FeignException;

@Service
public class CursoServiceImpl implements CursoService {
    @Autowired
    private CursoRepository repository;

    @Autowired
    UsuarioClientRest usuarioClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> agregarUsuarioACurso(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = repository.findById(cursoId);
        if (!cursoOptional.isPresent()) {
            return Optional.empty();
        }

        try {
            Usuario usuarioMicro = usuarioClientRest.detalle(usuario.getId());
            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMicro.getId());
            cursoUsuario.setCursoId(curso.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioMicro);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public boolean desasignarUsuarioDeCurso(Long usuarioId, Long cursoId) {
        Optional<Curso> cursoOptional = repository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            boolean removed = curso.getCursoUsuarios().removeIf(cursoUsuario -> cursoUsuario.getUsuarioId().equals(usuarioId));
            if (removed) {
                repository.save(curso);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuarioYAsignarACurso(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = repository.findById(cursoId);
        if (!cursoOptional.isPresent()) {
            return Optional.empty();
        }
        Curso curso = cursoOptional.get();

        // Aquí deberías implementar la lógica para crear el usuario en el microservicio de usuarios
        // Por simplicidad, asumiremos que el usuario ya está creado y lo obtenemos directamente
        Usuario usuarioCreado;
        try {
            usuarioCreado = usuarioClientRest.crear(usuario);
        } catch (FeignException e) {
            return Optional.empty();
        }

        CursoUsuario cursoUsuario = new CursoUsuario();
        cursoUsuario.setUsuarioId(usuarioCreado.getId());
        cursoUsuario.setCursoId(curso.getId());

        curso.addCursoUsuario(cursoUsuario);
        repository.save(curso);

        return Optional.of(usuarioCreado);
    }
}
