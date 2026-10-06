package supermercado.model;

import java.util.Objects;

public class Categoria {

    private Long id;
    private String nome;
    private Double percentualLucro;

    public Categoria(String nome, Double percentualLucro) {
        this.nome = nome;
        this.percentualLucro = percentualLucro;
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

    public Double getPercentualLucro() {
        return percentualLucro;
    }

    public void atualizar(String nome, Double percentualLucro) {
        this.nome = nome;
        this.percentualLucro = percentualLucro;
    }

    public boolean possuiNome(String outroNome) {
        return nome != null && outroNome != null
                && nome.trim().equalsIgnoreCase(outroNome.trim());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Categoria outra) || id == null) {
            return false;
        }
        return id.equals(outra.id);
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
