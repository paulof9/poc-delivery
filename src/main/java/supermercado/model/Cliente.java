package supermercado.model;

import java.util.Objects;

public class Cliente {

    private Long id;
    private String nome;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private TipoCliente tipo = TipoCliente.PRATA;
    private double totalCompras;

    public Cliente(String nome, String logradouro, String bairro, String cidade, String uf) {
        atualizar(nome, logradouro, bairro, cidade, uf);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUf() {
        return uf;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public double getTotalCompras() {
        return totalCompras;
    }

    public void atualizar(String nome, String logradouro, String bairro, String cidade, String uf) {
        this.nome = nome;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }

    public void registrarCompra(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da compra deve ser positivo.");
        }
        totalCompras += valor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cliente outro) || id == null) {
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
        return nome;
    }
}
