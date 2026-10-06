package supermercado.servico;

import supermercado.model.HistoricoPreco;
import supermercado.repositorio.HistoricoPrecoRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HistoricoPrecoService {

    private final HistoricoPrecoRepository historicoRepository;

    public HistoricoPrecoService(HistoricoPrecoRepository historicoRepository) {
        this.historicoRepository = Objects.requireNonNull(historicoRepository);
    }

    public List<HistoricoPreco> listarPorProduto(Long produtoId) {
        List<HistoricoPreco> historicos = new ArrayList<>(historicoRepository.buscarPorProduto(produtoId));
        Collections.reverse(historicos);
        historicos.sort(Comparator.comparing(HistoricoPreco::getDataCalculo).reversed());
        return historicos;
    }
}
