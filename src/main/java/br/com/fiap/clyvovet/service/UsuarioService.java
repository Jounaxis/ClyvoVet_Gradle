package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.dto.request.UsuarioRequest;
import br.com.fiap.clyvovet.dto.response.UsuarioResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.UsuarioMapper;
import br.com.fiap.clyvovet.model.Usuario;
import br.com.fiap.clyvovet.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    //CRUD

    //CREATE
    public UsuarioResponse create(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(request, usuario);
        usuario.setDataCriacao(LocalDateTime.now());

        return usuarioMapper.usuarioToResponse(usuarioRepository.save(usuario));
    }

    //READ

    public UsuarioResponse readUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return usuarioMapper.usuarioToResponse(usuario);
    }

    public Page<UsuarioResponse> read(Pageable pageable) {

        Page<UsuarioResponse> usuarios =
                usuarioRepository.findAll(pageable)
                        .map(usuarioMapper::usuarioToResponse);

        if (usuarios.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Não possui nenhum usuário cadastrado"
            );
        }

        return usuarios;
    }

    public UsuarioResponse readByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado pelo email"));

        return usuarioMapper.usuarioToResponse(usuario);
    }

    //UPDATE

    public UsuarioResponse update(Integer id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        BeanUtils.copyProperties(request, usuario);

        return usuarioMapper.usuarioToResponse(usuarioRepository.save(usuario));
    }

    //DELETE
    public void delete(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
    }

}
