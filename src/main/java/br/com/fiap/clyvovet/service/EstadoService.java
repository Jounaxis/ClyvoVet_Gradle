package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.dto.request.EstadoRequest;
import br.com.fiap.clyvovet.dto.response.EstadoResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.EstadoMapper;
import br.com.fiap.clyvovet.model.Estado;
import br.com.fiap.clyvovet.repository.EstadoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {
    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;

    public EstadoService(EstadoRepository estadoRepository, EstadoMapper estadoMapper) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
    }

    //CRUD

    //CREATE
    public EstadoResponse create(EstadoRequest request) {
        Estado estado = new Estado();
        BeanUtils.copyProperties(request, estado);
        return estadoMapper.estadoToResponse(estadoRepository.save(estado));
    }

    //READ

    public EstadoResponse readEstado(Integer id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado não encontrado"));
        return estadoMapper.estadoToResponse(estado);
    }

    public Page<EstadoResponse> read(Pageable pageable) {
        return estadoRepository.findAll(pageable).map(estadoMapper::estadoToResponse);
    }

    public EstadoResponse readByUf(String uf) {
        Estado estado = estadoRepository.findByUf(uf)
                .orElseThrow(() -> new ResourceNotFoundException("Estado não encontrado pela UF"));
        return estadoMapper.estadoToResponse(estado);
    }

    //UPDATE

    public EstadoResponse update(Integer id, EstadoRequest request) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado não encontrado"));

        BeanUtils.copyProperties(request, estado);
        return estadoMapper.estadoToResponse(estadoRepository.save(estado));
    }

    //DELETE

    public void delete(Integer id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado não encontrado"));

        estadoRepository.delete(estado);
    }
}


