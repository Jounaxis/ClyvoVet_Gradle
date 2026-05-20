package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.dto.request.EnderecoRequest;
import br.com.fiap.clyvovet.dto.response.EnderecoResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.EnderecoMapper;
import br.com.fiap.clyvovet.model.Bairro;
import br.com.fiap.clyvovet.model.Endereco;
import br.com.fiap.clyvovet.model.Usuario;
import br.com.fiap.clyvovet.repository.BairroRepository;
import br.com.fiap.clyvovet.repository.EnderecoRepository;
import br.com.fiap.clyvovet.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BairroRepository bairroRepository;
    private final EnderecoMapper enderecoMapper;

    public EnderecoService(
            EnderecoRepository enderecoRepository,
            UsuarioRepository usuarioRepository,
            BairroRepository bairroRepository,
            EnderecoMapper enderecoMapper
    ) {
        this.enderecoRepository = enderecoRepository;
        this.usuarioRepository = usuarioRepository;
        this.bairroRepository = bairroRepository;
        this.enderecoMapper = enderecoMapper;
    }


    //CRUD

    //CREATE

    public EnderecoResponse create(EnderecoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Bairro bairro = bairroRepository.findById(request.bairroId())
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));

        Endereco endereco = new Endereco();
        endereco.setLogradouro(request.logradouro());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());
        endereco.setCep(request.cep());
        endereco.setLatitude(request.latitude());
        endereco.setLongitude(request.longitude());
        endereco.setUsuario(usuario);
        endereco.setBairro(bairro);

        return enderecoMapper.enderecoToResponse(enderecoRepository.save(endereco));
    }

    //READ

    public EnderecoResponse readEndereco(Integer id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));

        return enderecoMapper.enderecoToResponse(endereco);
    }

    public Page<EnderecoResponse> read(Pageable pageable) {

        Page<EnderecoResponse> enderecos =
                enderecoRepository.findAll(pageable)
                        .map(enderecoMapper::enderecoToResponse);

        if (enderecos.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Não possui nenhum endereço cadastrado"
            );
        }

        return enderecos;
    }

    public Page<EnderecoResponse> readByCep(String cep, Pageable pageable) {
        return enderecoRepository.findByCep(cep, pageable)
                .map(enderecoMapper::enderecoToResponse);
    }

    //UPDATE

    public EnderecoResponse update(Integer id, EnderecoRequest request) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Bairro bairro = bairroRepository.findById(request.bairroId())
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));

        endereco.setLogradouro(request.logradouro());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());
        endereco.setCep(request.cep());
        endereco.setLatitude(request.latitude());
        endereco.setLongitude(request.longitude());
        endereco.setUsuario(usuario);
        endereco.setBairro(bairro);

        return enderecoMapper.enderecoToResponse(enderecoRepository.save(endereco));
    }

    //DELETE
    public void delete(Integer id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));

        enderecoRepository.delete(endereco);
    }

}
