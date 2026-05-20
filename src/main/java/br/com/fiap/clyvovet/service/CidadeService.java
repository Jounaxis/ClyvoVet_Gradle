package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.dto.request.CidadeRequest;
import br.com.fiap.clyvovet.dto.response.CidadeResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.CidadeMapper;
import br.com.fiap.clyvovet.model.Cidade;
import br.com.fiap.clyvovet.model.Estado;
import br.com.fiap.clyvovet.repository.CidadeRepository;
import br.com.fiap.clyvovet.repository.EstadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CidadeService {
    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;
    private final CidadeMapper cidadeMapper;

    public CidadeService(CidadeRepository cidadeRepository, EstadoRepository estadoRepository, CidadeMapper cidadeMapper) {
        this.cidadeRepository = cidadeRepository;
        this.estadoRepository = estadoRepository;
        this.cidadeMapper = cidadeMapper;
    }

    //CRUD

    //CREATE

    public CidadeResponse create(CidadeRequest request) {
        Estado estado = estadoRepository.findById(request.estadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado não encontrado"));

        Cidade cidade = new Cidade();
        cidade.setNome(request.nome());
        cidade.setEstado(estado);

        return cidadeMapper.cidadeToResponse(cidadeRepository.save(cidade));
    }

    //READ

    public CidadeResponse readCidade(Integer id) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        return cidadeMapper.cidadeToResponse(cidade);
    }

    public Page<CidadeResponse> read(Pageable pageable) {

        Page<CidadeResponse> cidades =
                cidadeRepository.findAll(pageable)
                        .map(cidadeMapper::cidadeToResponse);

        if (cidades.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Não possui nenhuma cidade cadastrada"
            );
        }

        return cidades;
    }

    public Page<CidadeResponse> readByNome(
            String nome,
            Pageable pageable
    ) {

        Page<CidadeResponse> cidades =
                cidadeRepository
                        .findByNomeContainingIgnoreCase(nome, pageable)
                        .map(cidadeMapper::cidadeToResponse);

        if (cidades.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Nenhuma cidade encontrada com esse nome"
            );
        }

        return cidades;
    }

    //UPDATE

    public CidadeResponse update(Integer id, CidadeRequest request) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        Estado estado = estadoRepository.findById(request.estadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado não encontrado"));

        cidade.setNome(request.nome());
        cidade.setEstado(estado);

        return cidadeMapper.cidadeToResponse(cidadeRepository.save(cidade));
    }

    //DELETE

    public void delete(Integer id) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        cidadeRepository.delete(cidade);
    }
}
