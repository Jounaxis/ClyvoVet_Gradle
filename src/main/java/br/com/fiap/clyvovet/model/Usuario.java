package br.com.fiap.clyvovet.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_USUARIO")
public class Usuario {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "usuario_sequence"
    )
    @SequenceGenerator(
            name = "usuario_sequence",
            sequenceName = "USUARIO_SEQUENCE",
            allocationSize = 1
    )

    @Column(name = "ID_USUARIO")
    private Integer usuarioId;

    @Column(name = "EMAIL", length = 80, nullable = false)
    private String email;

    @Column(name = "SENHA", length = 100, nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_USER", nullable = false)
    private TipoUsuario tipoUser;

    @Column(name = "DATA_CRIACAO")
    private LocalDateTime dataCriacao;

    //Construtor Vazio


    public Usuario() {
    }

    //GET E SET


    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(TipoUsuario tipoUser) {
        this.tipoUser = tipoUser;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
