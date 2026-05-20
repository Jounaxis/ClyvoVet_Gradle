package br.com.fiap.clyvovet.service;
import br.com.fiap.clyvovet.dto.request.AnexoRequest;
import br.com.fiap.clyvovet.dto.response.AnexoResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.AnexoMapper;
import br.com.fiap.clyvovet.model.Anexo;
import br.com.fiap.clyvovet.model.TipoArquivo;
import br.com.fiap.clyvovet.model.Usuario;
import br.com.fiap.clyvovet.repository.AnexoRepository;
import br.com.fiap.clyvovet.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnexoService {
    private final AnexoRepository anexoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnexoMapper anexoMapper;

    public AnexoService(AnexoRepository anexoRepository, UsuarioRepository usuarioRepository, AnexoMapper anexoMapper) {
        this.anexoRepository = anexoRepository;
        this.usuarioRepository = usuarioRepository;
        this.anexoMapper = anexoMapper;
    }

    //CRUD

    //CREATE

    public AnexoResponse create(AnexoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Anexo anexo = new Anexo();
        anexo.setNomeArquivo(request.nomeArquivo());
        anexo.setTipoArquivo(request.tipoArquivo());
        anexo.setUrl(request.url());
        anexo.setTamanhoKb(request.tamanhoKb());
        anexo.setDataCriacao(
                request.dataCriacao() != null ? request.dataCriacao() : LocalDateTime.now()
        );
        anexo.setUsuario(usuario);

        return anexoMapper.anexoToResponse(anexoRepository.save(anexo));
    }

    //READ

    public AnexoResponse readAnexo(Integer id) {
        Anexo anexo = anexoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anexo não encontrado"));

        return anexoMapper.anexoToResponse(anexo);
    }

    public Page<AnexoResponse> read(Pageable pageable) {

        Page<AnexoResponse> anexos =
                anexoRepository.findAll(pageable)
                        .map(anexoMapper::anexoToResponse);

        if (anexos.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Não possui nenhum anexo cadastrado"
            );
        }

        return anexos;
    }

    public Page<AnexoResponse> readByTipoArquivo(TipoArquivo tipoArquivo, Pageable pageable) {
        return anexoRepository.findByTipoArquivo(tipoArquivo, pageable)
                .map(anexoMapper::anexoToResponse);
    }

    //UPDATE

    public AnexoResponse update(Integer id, AnexoRequest request) {
        Anexo anexo = anexoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anexo não encontrado"));

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        anexo.setNomeArquivo(request.nomeArquivo());
        anexo.setTipoArquivo(request.tipoArquivo());
        anexo.setUrl(request.url());
        anexo.setTamanhoKb(request.tamanhoKb());
        anexo.setDataCriacao(request.dataCriacao());
        anexo.setUsuario(usuario);

        return anexoMapper.anexoToResponse(anexoRepository.save(anexo));
    }

    //DELETE

    public void delete(Integer id) {
        Anexo anexo = anexoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anexo não encontrado"));

        anexoRepository.delete(anexo);
    }
}
