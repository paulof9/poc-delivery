package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.model.Categoria;
import supermercado.view.CategoriaView;

class CategoriaInclusaoEstado extends CategoriaEstado {

    CategoriaInclusaoEstado(CategoriaPresenter presenter) {
        super(presenter);
    }

    @Override
    void entrar() {
        CategoriaView view = presenter.getView();
        view.setModo("Modo: Inclusão");
        view.setNome("");
        view.setPercentual("");
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
            Categoria nova = presenter.getCategoriaService().incluir(view.getNome(), presenter.lerPercentual());
            view.mostrarSucesso("Item salvo com sucesso!");
            presenter.carregarTabela(nova.getId());
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
