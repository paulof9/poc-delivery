package supermercado.model;

import java.util.Objects;

public class Produto {

    private Long id;
    private String nome;
    private Double precoCusto;
    private Categoria categoria;
    private Double margemLucroAtual;
    private Double precoVendaAtual;

    public Produto(String nome, Double precoCusto, Categoria categoria) {
        this.nome = nome;
        this.precoCusto = precoCusto;
        this.categoria = categoria;
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

    public Double getPrecoCusto() {
        return precoCusto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Double getMargemLucroAtual() {
        return margemLucroAtual;
    }

    public Double getPrecoVendaAtual() {
        return precoVendaAtual;
    }

    public void atualizarDados(String nome, Double precoCusto, Categoria categoria) {
        this.nome = nome;
        this.precoCusto = precoCusto;
        this.categoria = categoria;
    }

    public void registrarCalculo(Double percentualLucro, Double precoVenda) {
        this.margemLucroAtual = percentualLucro;
        this.precoVendaAtual = precoVenda;
    }

    public boolean possuiCalculo() {
        return precoVendaAtual != null;
    }

    public boolean pertenceACategoria(Long categoriaId) {
        return categoria != null && Objects.equals(categoria.getId(), categoriaId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Produto outro) || id == null) {
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
