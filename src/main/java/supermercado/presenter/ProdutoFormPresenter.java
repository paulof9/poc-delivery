package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.model.Categoria;
import supermercado.model.Produto;
import supermercado.servico.CategoriaService;
import supermercado.servico.NotificadorAlteracoes;
import supermercado.servico.ObservadorAlteracao;
import supermercado.servico.ProdutoService;
import supermercado.servico.TipoAlteracao;
import supermercado.view.ProdutoFormView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProdutoFormPresenter {

    private final ProdutoFormView view;
    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final NotificadorAlteracoes notificador;
    private final Long produtoId;
    private final ObservadorAlteracao observador = this::dadosAlterados;
    private final List<Long> idsCategorias = new ArrayList<>();

    public ProdutoFormPresenter(ProdutoFormView view, ProdutoService produtoService,
            CategoriaService categoriaService, NotificadorAlteracoes notificador, Long produtoId) {
        this.view = Objects.requireNonNull(view);
        this.produtoService = Objects.requireNonNull(produtoService);
        this.categoriaService = Objects.requireNonNull(categoriaService);
        this.notificador = Objects.requireNonNull(notificador);
        this.produtoId = produtoId;

        view.setAcaoSalvar(this::salvar);
        view.setAcaoCancelar(this::fechar);
        view.setAcaoFechar(this::fechar);
        notificador.registrar(observador);

        if (isEdicao()) {
            carregarProduto();
        } else {
            carregarCategorias(null);
            view.setMargemLucro("");
            view.setPrecoVenda("");
        }
    }

    private boolean isEdicao() {
        return produtoId != null;
    }

    private Produto buscarProduto() {
        return produtoService.buscarPorId(produtoId)
                .orElseThrow(() -> new IllegalStateException("Produto não encontrado: " + produtoId));
    }

    private void carregarProduto() {
        Produto produto = buscarProduto();
        view.setNome(produto.getNome());
        view.setPrecoCusto(Formatador.formatarDecimal(produto.getPrecoCusto()));
        carregarCategorias(produto.getCategoria().getId());
        exibirValoresCalculados(produto);
    }

    private void exibirValoresCalculados(Produto produto) {
        view.setMargemLucro(Formatador.formatarDecimal(produto.getMargemLucroAtual()));
        view.setPrecoVenda(Formatador.formatarMoeda(produto.getPrecoVendaAtual()));
    }

    private void carregarCategorias(Long idParaSelecionar) {
        idsCategorias.clear();
        List<String> nomes = new ArrayList<>();
        for (Categoria categoria : categoriaService.listarTodas()) {
            idsCategorias.add(categoria.getId());
            nomes.add(categoria.getNome());
        }
        view.setCategorias(nomes);
        view.setCategoriaSelecionada(idsCategorias.indexOf(idParaSelecionar));
    }

    private Long getCategoriaSelecionadaId() {
        int indice = view.getCategoriaSelecionada();
        return indice >= 0 && indice < idsCategorias.size() ? idsCategorias.get(indice) : null;
    }

    private void salvar() {
        try {
            String nome = view.getNome();
            Double precoCusto = Formatador.lerDecimal(view.getPrecoCusto(), "Preço de custo");
            Long categoriaId = getCategoriaSelecionadaId();
            if (isEdicao()) {
                produtoService.alterar(produtoId, nome, precoCusto, categoriaId);
            } else {
                produtoService.incluir(nome, precoCusto, categoriaId);
            }
            view.mostrarSucesso("Item salvo com sucesso!");
            fechar();
        } catch (NegocioException e) {
            view.mostrarAviso(e.getMessage());
        }
    }

    private void dadosAlterados(TipoAlteracao tipo) {
        if (tipo == TipoAlteracao.CATEGORIAS) {
            carregarCategorias(getCategoriaSelecionadaId());
        } else if (tipo == TipoAlteracao.PRECOS && isEdicao()) {
            produtoService.buscarPorId(produtoId).ifPresent(this::exibirValoresCalculados);
        }
    }

    private void fechar() {
        notificador.remover(observador);
        view.fechar();
    }
}
