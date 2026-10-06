package supermercado.presenter;

import supermercado.model.Produto;
import supermercado.servico.CriterioBuscaProduto;
import supermercado.servico.NotificadorAlteracoes;
import supermercado.servico.ObservadorAlteracao;
import supermercado.servico.ProdutoService;
import supermercado.servico.TipoAlteracao;
import supermercado.view.BuscaProdutoView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BuscaProdutoPresenter {

    private static final CriterioBuscaProduto[] CRITERIOS = CriterioBuscaProduto.values();

    private final BuscaProdutoView view;
    private final ProdutoService produtoService;
    private final NotificadorAlteracoes notificador;
    private final Navegador navegador;
    private final ObservadorAlteracao observador = this::dadosAlterados;
    private final List<Long> idsProdutos = new ArrayList<>();
    private CriterioBuscaProduto criterioPesquisado = CriterioBuscaProduto.NOME;
    private String textoPesquisado = "";

    public BuscaProdutoPresenter(BuscaProdutoView view, ProdutoService produtoService,
            NotificadorAlteracoes notificador, Navegador navegador) {
        this.view = Objects.requireNonNull(view);
        this.produtoService = Objects.requireNonNull(produtoService);
        this.notificador = Objects.requireNonNull(notificador);
        this.navegador = navegador;

        view.setCriteriosBusca(Arrays.stream(CRITERIOS).map(CriterioBuscaProduto::getDescricao).toList());
        view.setAcaoBuscar(this::buscar);
        view.setAcaoNovo(this::novo);
        view.setAcaoVisualizar(this::visualizar);
        view.setAcaoSelecaoAlterada(this::atualizarBotaoVisualizar);
        view.setAcaoFechar(this::fechar);
        notificador.registrar(observador);

        pesquisar(null);
    }

    private void buscar() {
        int indice = view.getCriterioSelecionado();
        criterioPesquisado = indice >= 0 ? CRITERIOS[indice] : CriterioBuscaProduto.NOME;
        textoPesquisado = view.getTextoBusca();
        pesquisar(null);
    }

    private void pesquisar(Long idParaSelecionar) {
        List<Produto> produtos = produtoService.buscar(criterioPesquisado, textoPesquisado);
        idsProdutos.clear();
        List<String[]> linhas = new ArrayList<>();
        for (Produto produto : produtos) {
            idsProdutos.add(produto.getId());
            linhas.add(new String[]{
                produto.getNome(),
                Formatador.formatarMoeda(produto.getPrecoCusto()),
                produto.getCategoria().getNome(),
                Formatador.formatarDecimal(produto.getMargemLucroAtual()),
                Formatador.formatarMoeda(produto.getPrecoVendaAtual())
            });
        }
        view.setProdutos(linhas);
        int linha = idsProdutos.indexOf(idParaSelecionar);
        if (linha >= 0) {
            view.selecionarLinha(linha);
        }
        atualizarBotaoVisualizar();
    }

    private Long getIdSelecionado() {
        int linha = view.getLinhaSelecionada();
        return linha >= 0 && linha < idsProdutos.size() ? idsProdutos.get(linha) : null;
    }

    private void atualizarBotaoVisualizar() {
        view.setVisualizarHabilitado(getIdSelecionado() != null);
    }

    private void novo() {
        navegador.abrirInclusaoProduto();
    }

    private void visualizar() {
        Long id = getIdSelecionado();
        if (id != null) {
            navegador.abrirVisualizacaoProduto(id);
        }
    }

    private void dadosAlterados(TipoAlteracao tipo) {
        pesquisar(getIdSelecionado());
    }

    private void fechar() {
        notificador.remover(observador);
        view.fechar();
    }
}
