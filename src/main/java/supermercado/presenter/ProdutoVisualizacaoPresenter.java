package supermercado.presenter;

import supermercado.model.Produto;
import supermercado.servico.NotificadorAlteracoes;
import supermercado.servico.ObservadorAlteracao;
import supermercado.servico.ProdutoService;
import supermercado.servico.TipoAlteracao;
import supermercado.view.ProdutoVisualizacaoView;
import java.util.Objects;

public class ProdutoVisualizacaoPresenter {

    private final ProdutoVisualizacaoView view;
    private final ProdutoService produtoService;
    private final NotificadorAlteracoes notificador;
    private final Navegador navegador;
    private final Long produtoId;
    private final ObservadorAlteracao observador = this::dadosAlterados;

    public ProdutoVisualizacaoPresenter(ProdutoVisualizacaoView view, ProdutoService produtoService,
            NotificadorAlteracoes notificador, Navegador navegador, Long produtoId) {
        this.view = Objects.requireNonNull(view);
        this.produtoService = Objects.requireNonNull(produtoService);
        this.notificador = Objects.requireNonNull(notificador);
        this.navegador = navegador;
        this.produtoId = Objects.requireNonNull(produtoId);

        view.setAcaoHistorico(() -> navegador.abrirHistoricoPrecos(produtoId));
        view.setAcaoEditar(() -> navegador.abrirEdicaoProduto(produtoId));
        view.setAcaoFechar(this::fechar);
        notificador.registrar(observador);

        carregar();
    }

    private void carregar() {
        Produto produto = produtoService.buscarPorId(produtoId)
                .orElseThrow(() -> new IllegalStateException("Produto não encontrado: " + produtoId));
        view.setNome(produto.getNome());
        view.setPrecoCusto(Formatador.formatarMoeda(produto.getPrecoCusto()));
        view.setCategoria(produto.getCategoria().getNome());
        view.setMargemLucro(Formatador.formatarDecimal(produto.getMargemLucroAtual()));
        view.setPrecoVenda(Formatador.formatarMoeda(produto.getPrecoVendaAtual()));
    }

    private void dadosAlterados(TipoAlteracao tipo) {
        carregar();
    }

    private void fechar() {
        notificador.remover(observador);
        view.fechar();
    }
}
