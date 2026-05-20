package br.com.fiap.clyvovet.service;

import br.com.fiap.clyvovet.dto.request.TutorRequest;
import br.com.fiap.clyvovet.dto.response.TutorResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.TutorMapper;
import br.com.fiap.clyvovet.model.Tutor;
import br.com.fiap.clyvovet.model.Usuario;
import br.com.fiap.clyvovet.repository.TutorRepository;
import br.com.fiap.clyvovet.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service

public class TutorService {
    private final TutorRepository tutorRepository;
    private final UsuarioRepository usuarioRepository;
    private final TutorMapper tutorMapper;

    public TutorService(TutorRepository tutorRepository, UsuarioRepository usuarioRepository, TutorMapper tutorMapper) {
        this.tutorRepository = tutorRepository;
        this.usuarioRepository = usuarioRepository;
        this.tutorMapper = tutorMapper;
    }

    //CRUD

    //CREATE

    public TutorResponse create(TutorRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Tutor tutor = new Tutor();
        tutor.setNomeTutor(request.nomeTutor());
        tutor.setCpf(request.cpf());
        tutor.setTelefone(request.telefone());
        tutor.setDataNascimento(request.dataNascimento());
        tutor.setUsuario(usuario);

        return tutorMapper.tutorToResponse(tutorRepository.save(tutor));
    }

    //READ

    public TutorResponse readTutor(Integer id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado"));

        return tutorMapper.tutorToResponse(tutor);
    }

    public Page<TutorResponse> read(Pageable pageable) {
        return tutorRepository.findAll(pageable)
                .map(tutorMapper::tutorToResponse);
    }

    public TutorResponse readByCpf(String cpf) {
        Tutor tutor = tutorRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado pelo CPF"));

        return tutorMapper.tutorToResponse(tutor);
    }

    //UPDATE

    public TutorResponse update(Integer id, TutorRequest request) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado"));

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        tutor.setNomeTutor(request.nomeTutor());
        tutor.setCpf(request.cpf());
        tutor.setTelefone(request.telefone());
        tutor.setDataNascimento(request.dataNascimento());
        tutor.setUsuario(usuario);

        return tutorMapper.tutorToResponse(tutorRepository.save(tutor));
    }

    //DELETE

    public void delete(Integer id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado"));

        tutorRepository.delete(tutor);
    }
}
