package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.model.Categoria;
import supermercado.view.CategoriaView;
import java.util.Optional;

class CategoriaVisualizacaoEstado extends CategoriaEstado {

    CategoriaVisualizacaoEstado(CategoriaPresenter presenter) {
        super(presenter);
    }

    @Override
    void entrar() {
        CategoriaView view = presenter.getView();
        view.setModo("Modo: Visualização");
        view.setCamposEditaveis(false);
        view.setTabelaHabilitada(true);
        view.setFechamentoPermitido(true);
        selecaoAlterada();
    }

    @Override
    void selecaoAlterada() {
        presenter.exibirCategoriaSelecionada();
        boolean selecionada = presenter.getCategoriaSelecionada().isPresent();
        presenter.getView().setBotoesHabilitados(true, selecionada, selecionada, false, false, true);
    }

    @Override
    void novo() {
        presenter.setEstado(new CategoriaInclusaoEstado(presenter));
    }

    @Override
    void editar() {
        presenter.getCategoriaSelecionada().ifPresent(categoria
                -> presenter.setEstado(new CategoriaEdicaoEstado(presenter, categoria.getId())));
    }

    @Override
    void excluir() {
        Optional<Categoria> selecionada = presenter.getCategoriaSelecionada();
        if (selecionada.isEmpty()) {
            return;
        }
        Categoria categoria = selecionada.get();
        CategoriaView view = presenter.getView();
        if (!view.confirmar("Confirmação de exclusão",
                "Deseja realmente excluir a categoria \"" + categoria.getNome() + "\"?")) {
            return;
        }
        try {
            presenter.getCategoriaService().excluir(categoria.getId());
            view.mostrarSucesso("Item \"" + categoria.getNome() + "\" excluído com sucesso!");
            presenter.carregarTabela(null);
            selecaoAlterada();
        } catch (NegocioException e) {
            view.mostrarAviso(e.getMessage());
        }
    }

    @Override
    void fechar() {
        presenter.fechar();
    }
}
