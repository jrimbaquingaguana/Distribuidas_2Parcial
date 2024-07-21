package com.espe.msvc_cursos.services;
import com.espe.msvc_cursos.models.Usuario;
import com.espe.msvc_cursos.clients.UsuarioClientRest;
import com.espe.msvc_cursos.models.entity.CursoUsuario;
import com.espe.msvc_cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation. Transactional;
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
    public Optional<Usuario> agregarUsuario(Usuario usuario, Long idCurso) {
        // Verificar que existe el curso.
        Optional<Curso> cursoOptional = repository.findById(idCurso);
        if (!cursoOptional.isPresent()) {
            // Si el curso no existe, devolver Optional.empty()
            return Optional.empty();
        }

        try {
            // Verificar que existe el usuario y obtenerlo.
            Usuario usuarioMicro = usuarioClientRest.detalle(usuario.getId());

            // Actualizar el cursoId del usuario con el id del curso.
            usuarioMicro.setCursoId(idCurso);

            // Guardar el usuario actualizado.
            usuarioMicro = usuarioClientRest.editar(usuarioMicro.getId(), usuarioMicro);

            // Agregar el usuario al curso.
            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMicro.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);

            // Devolver el usuario agregado.
            return Optional.of(usuarioMicro);
        } catch (FeignException.NotFound e) {
            // Si el usuario no existe, devolver Optional.empty()
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario, Long idCurso) {
        //Verificar que exista el curso.
        Optional<Curso> o = repository.findById(idCurso);
        if (o.isPresent()){
            //Verificar que exista el usuario.
            Usuario usuarioMicro = usuarioClientRest.crear(usuario);

            //Agregar el usuario al curso.
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuario.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean eliminarUsuario(Long usuarioId, Long idCurso) {
        Optional<Curso> cursoOptional = repository.findById(idCurso);
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
}
