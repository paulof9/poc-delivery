package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.model.Cliente;
import supermercado.model.TipoCliente;
import supermercado.view.ClienteView;

class ClienteInclusaoEstado extends ClienteEstado {

    ClienteInclusaoEstado(ClientePresenter presenter) {
        super(presenter);
    }

    @Override
    void entrar() {
        ClienteView view = presenter.getView();
        view.setModo("Modo: Inclusão");
        presenter.limparCampos();
        view.setTipoCliente(TipoCliente.PRATA.name());
        view.setTotalCompras(Formatador.formatarDecimal(0.0));
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
            Cliente novo = presenter.getClienteService().incluir(view.getNome(), view.getLogradouro(),
                    view.getBairro(), view.getCidade(), view.getUf());
            view.mostrarSucesso("Cliente salvo com sucesso!");
            presenter.carregarTabela(novo.getId());
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
