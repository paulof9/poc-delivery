package supermercado.model;

import java.time.LocalDate;
import java.util.Objects;

public final class HistoricoPreco {

    private final Long produtoId;
    private final LocalDate dataCalculo;
    private final Double percentualLucro;
    private final Double precoVenda;

    public HistoricoPreco(Long produtoId, LocalDate dataCalculo, Double percentualLucro, Double precoVenda) {
        this.produtoId = Objects.requireNonNull(produtoId, "produtoId");
        this.dataCalculo = Objects.requireNonNull(dataCalculo, "dataCalculo");
        this.percentualLucro = Objects.requireNonNull(percentualLucro, "percentualLucro");
        this.precoVenda = Objects.requireNonNull(precoVenda, "precoVenda");
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public LocalDate getDataCalculo() {
        return dataCalculo;
    }

    public Double getPercentualLucro() {
        return percentualLucro;
    }

    public Double getPrecoVenda() {
        return precoVenda;
    }
}
