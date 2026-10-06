package supermercado.repositorio;

import supermercado.model.HistoricoPreco;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HistoricoPrecoRepository {

    void salvar(HistoricoPreco historico);

    List<HistoricoPreco> buscarPorProduto(Long produtoId);

    Optional<LocalDate> buscarDataUltimoCalculo();
}
