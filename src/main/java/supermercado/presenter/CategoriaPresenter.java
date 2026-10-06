package supermercado.presenter;

import supermercado.excecao.ValidacaoException;
import supermercado.model.Categoria;
import supermercado.servico.CategoriaService;
import supermercado.view.CategoriaView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CategoriaPresenter {

    private final CategoriaView view;
    private final CategoriaService categoriaService;
    private final List<Long> idsCategorias = new ArrayList<>();
    private CategoriaEstado estado;

    public CategoriaPresenter(CategoriaView view, CategoriaService categoriaService) {
        this.view = Objects.requireNonNull(view);
        this.categoriaService = Objects.requireNonNull(categoriaService);

        view.setAcaoNovo(() -> estado.novo());
        view.setAcaoEditar(() -> estado.editar());
        view.setAcaoExcluir(() -> estado.excluir());
        view.setAcaoSalvar(() -> estado.salvar());
        view.setAcaoCancelar(() -> estado.cancelar());
        view.setAcaoSelecaoAlterada(() -> estado.selecaoAlterada());
        view.setAcaoFechar(() -> estado.fechar());

        estado = new CategoriaVisualizacaoEstado(this);
        carregarTabela(null);
        estado.entrar();
    }

    CategoriaView getView() {
        return view;
    }

    CategoriaService getCategoriaService() {
        return categoriaService;
    }

    CategoriaEstado getEstado() {
        return estado;
    }

    void setEstado(CategoriaEstado novoEstado) {
        estado = novoEstado;
        estado.entrar();
    }

    void carregarTabela(Long idParaSelecionar) {
        idsCategorias.clear();
        List<String[]> linhas = new ArrayList<>();
        for (Categoria categoria : categoriaService.listarTodas()) {
            idsCategorias.add(categoria.getId());
            linhas.add(new String[]{categoria.getNome(), Formatador.formatarDecimal(categoria.getPercentualLucro())});
        }
        view.setCategorias(linhas);
        int linha = idsCategorias.indexOf(idParaSelecionar);
        if (linha < 0 && !idsCategorias.isEmpty()) {
            linha = 0;
        }
        if (linha >= 0) {
            view.selecionarLinha(linha);
        }
    }

    Optional<Categoria> getCategoriaSelecionada() {
        int linha = view.getLinhaSelecionada();
        if (linha < 0 || linha >= idsCategorias.size()) {
            return Optional.empty();
        }
        return categoriaService.buscarPorId(idsCategorias.get(linha));
    }

    void exibirCategoriaSelecionada() {
        Optional<Categoria> categoria = getCategoriaSelecionada();
        view.setNome(categoria.map(Categoria::getNome).orElse(""));
        view.setPercentual(categoria.map(c -> Formatador.formatarDecimal(c.getPercentualLucro())).orElse(""));
    }

    Double lerPercentual() throws ValidacaoException {
        return Formatador.lerDecimal(view.getPercentual(), "Percentual de lucro (%)");
    }

    void fechar() {
        view.fechar();
    }
}
