package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.view.CategoriaView;

class CategoriaEdicaoEstado extends CategoriaEstado {

    private final Long categoriaId;

    CategoriaEdicaoEstado(CategoriaPresenter presenter, Long categoriaId) {
        super(presenter);
        this.categoriaId = categoriaId;
    }

    @Override
    void entrar() {
        CategoriaView view = presenter.getView();
        view.setModo("Modo: Edição");
        view.setCamposEditaveis(true);
        view.setTabelaHabilitada(false);
        view.setFechamentoPermitido(false);
        view.setBotoesHabilitados(false, false, false, true, true, false);
        view.focarNome();
    }

    @Override
    void salvar() {
        CategoriaView view = presenter.getView();
        try {
            presenter.getCategoriaService().alterar(categoriaId, view.getNome(), presenter.lerPercentual());
            view.mostrarSucesso("Item salvo com sucesso!");
            presenter.carregarTabela(categoriaId);
            presenter.setEstado(new CategoriaVisualizacaoEstado(presenter));
        } catch (NegocioException e) {
            view.mostrarAviso(e.getMessage());
        }
    }

    @Override
    void cancelar() {
        presenter.setEstado(new CategoriaVisualizacaoEstado(presenter));
    }
}
