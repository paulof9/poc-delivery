package supermercado.repositorio;

import supermercado.model.HistoricoPreco;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HistoricoPrecoRepositoryMemoria implements HistoricoPrecoRepository {

    private final List<HistoricoPreco> historicos = new ArrayList<>();

    @Override
    public void salvar(HistoricoPreco historico) {
        historicos.add(Objects.requireNonNull(historico, "historico"));
    }

    @Override
    public List<HistoricoPreco> buscarPorProduto(Long produtoId) {
        return historicos.stream()
                .filter(historico -> historico.getProdutoId().equals(produtoId))
                .toList();
    }

    @Override
    public Optional<LocalDate> buscarDataUltimoCalculo() {
        return historicos.stream()
                .map(HistoricoPreco::getDataCalculo)
                .max(LocalDate::compareTo);
    }
}
