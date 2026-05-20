package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.dto.request.BairroRequest;
import br.com.fiap.clyvovet.dto.response.BairroResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.BairroMapper;
import br.com.fiap.clyvovet.model.Bairro;
import br.com.fiap.clyvovet.model.Cidade;
import br.com.fiap.clyvovet.repository.BairroRepository;
import br.com.fiap.clyvovet.repository.CidadeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BairroService {
    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;
    private final BairroMapper bairroMapper;

    public BairroService(BairroRepository bairroRepository, CidadeRepository cidadeRepository, BairroMapper bairroMapper) {
        this.bairroRepository = bairroRepository;
        this.cidadeRepository = cidadeRepository;
        this.bairroMapper = bairroMapper;
    }

    //CRUD

    //CREATE
    public BairroResponse create(BairroRequest request) {
        Cidade cidade = cidadeRepository.findById(request.cidadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        Bairro bairro = new Bairro();
        bairro.setNome(request.nome());
        bairro.setCidade(cidade);

        return bairroMapper.bairroToResponse(bairroRepository.save(bairro));
    }

    //READ

    public BairroResponse readBairro(Integer id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));

        return bairroMapper.bairroToResponse(bairro);
    }

    public Page<BairroResponse> read(Pageable pageable) {

        Page<BairroResponse> bairros =
                bairroRepository.findAll(pageable)
                        .map(bairroMapper::bairroToResponse);

        if (bairros.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Não possui nenhum bairro cadastrado"
            );
        }

        return bairros;
    }
    public Page<BairroResponse> readByNome(String nome, Pageable pageable) {
        return bairroRepository.findByNomeContainingIgnoreCase(nome, pageable)
                .map(bairroMapper::bairroToResponse);
    }

    //UPDATE

    public BairroResponse update(Integer id, BairroRequest request) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));

        Cidade cidade = cidadeRepository.findById(request.cidadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        bairro.setNome(request.nome());
        bairro.setCidade(cidade);

        return bairroMapper.bairroToResponse(bairroRepository.save(bairro));
    }

    //DELETE

    public void delete(Integer id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));

        bairroRepository.delete(bairro);
    }
}
