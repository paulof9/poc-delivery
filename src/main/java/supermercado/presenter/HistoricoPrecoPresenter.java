package supermercado.presenter;

import supermercado.model.HistoricoPreco;
import supermercado.model.Produto;
import supermercado.servico.HistoricoPrecoService;
import supermercado.servico.NotificadorAlteracoes;
import supermercado.servico.ObservadorAlteracao;
import supermercado.servico.ProdutoService;
import supermercado.servico.TipoAlteracao;
import supermercado.view.HistoricoPrecoView;
import java.util.List;
import java.util.Objects;

public class HistoricoPrecoPresenter {

    private final HistoricoPrecoView view;
    private final ProdutoService produtoService;
    private final HistoricoPrecoService historicoPrecoService;
    private final NotificadorAlteracoes notificador;
    private final Long produtoId;
    private final ObservadorAlteracao observador = this::dadosAlterados;

    public HistoricoPrecoPresenter(HistoricoPrecoView view, ProdutoService produtoService,
            HistoricoPrecoService historicoPrecoService, NotificadorAlteracoes notificador, Long produtoId) {
        this.view = Objects.requireNonNull(view);
        this.produtoService = Objects.requireNonNull(produtoService);
        this.historicoPrecoService = Objects.requireNonNull(historicoPrecoService);
        this.notificador = Objects.requireNonNull(notificador);
        this.produtoId = Objects.requireNonNull(produtoId);

        view.setAcaoFechar(this::fechar);
        notificador.registrar(observador);

        carregar();
    }

    private void carregar() {
        Produto produto = produtoService.buscarPorId(produtoId)
                .orElseThrow(() -> new IllegalStateException("Produto não encontrado: " + produtoId));
        view.setProduto(produto.getNome());
        view.setCategoria(produto.getCategoria().getNome());

        List<HistoricoPreco> historicos = historicoPrecoService.listarPorProduto(produtoId);
        view.setHistorico(historicos.stream()
                .map(historico -> new String[]{
                    Formatador.formatarData(historico.getDataCalculo()),
                    Formatador.formatarDecimal(historico.getPercentualLucro()),
                    Formatador.formatarMoeda(historico.getPrecoVenda())
                })
                .toList());
    }

    private void dadosAlterados(TipoAlteracao tipo) {
        carregar();
    }

    private void fechar() {
        notificador.remover(observador);
        view.fechar();
    }
}
