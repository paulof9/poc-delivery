package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.view.ClienteView;

class ClienteEdicaoEstado extends ClienteEstado {

    private final Long clienteId;

    ClienteEdicaoEstado(ClientePresenter presenter, Long clienteId) {
        super(presenter);
        this.clienteId = clienteId;
    }

    @Override
    void entrar() {
        ClienteView view = presenter.getView();
        view.setModo("Modo: Edição");
        view.setCamposEditaveis(true);
        view.setTabelaHabilitada(false);
        view.setFechamentoPermitido(false);
        view.setBotoesHabilitados(false, false, false, true, true, false);
        view.focarNome();
    }

    @Override
    void salvar() {
        ClienteView view = presenter.getView();
        try {
            presenter.getClienteService().alterar(clienteId, view.getNome(), view.getLogradouro(),
                    view.getBairro(), view.getCidade(), view.getUf());
            view.mostrarSucesso("Cliente salvo com sucesso!");
            presenter.carregarTabela(clienteId);
            presenter.setEstado(new ClienteVisualizacaoEstado(presenter));
        } catch (NegocioException e) {
            view.mostrarAviso(e.getMessage());
        }
    }

    @Override
    void cancelar() {
        presenter.setEstado(new ClienteVisualizacaoEstado(presenter));
    }
}
