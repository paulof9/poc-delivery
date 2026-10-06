package supermercado.model;

import java.util.Objects;

public class Usuario {

    private Long id;
    private String nomeCompleto;
    private String email;
    private String nomeUsuario;
    private String senha;
    private PerfilUsuario perfil;
    private StatusUsuario status = StatusUsuario.HABILITADO;
    private Cliente cliente;

    public Usuario(String nomeCompleto, String email, String nomeUsuario, String senha,
            PerfilUsuario perfil, Cliente cliente) {
        atualizar(nomeCompleto, email, nomeUsuario, perfil, cliente);
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public boolean isAdministrador() {
        return perfil == PerfilUsuario.ADMINISTRADOR;
    }

    public boolean isHabilitado() {
        return status == StatusUsuario.HABILITADO;
    }

    public boolean possuiSenha(String senhaInformada) {
        return senha != null && senha.equals(senhaInformada);
    }

    public void atualizar(String nomeCompleto, String email, String nomeUsuario,
            PerfilUsuario perfil, Cliente cliente) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.perfil = perfil;
        this.cliente = cliente;
    }

    public void alterarSenha(String novaSenha) {
        this.senha = novaSenha;
    }

    public void habilitar() {
        status = StatusUsuario.HABILITADO;
    }

    public void desabilitar() {
        status = StatusUsuario.DESABILITADO;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Usuario outro) || id == null) {
            return false;
        }
        return id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return id == null ? System.identityHashCode(this) : Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return nomeCompleto;
    }
}
