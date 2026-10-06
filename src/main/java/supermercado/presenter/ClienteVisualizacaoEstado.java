package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.model.Cliente;
import supermercado.view.ClienteView;
import java.util.Optional;

class ClienteVisualizacaoEstado extends ClienteEstado {

    ClienteVisualizacaoEstado(ClientePresenter presenter) {
        super(presenter);
    }

    @Override
    void entrar() {
        ClienteView view = presenter.getView();
        view.setModo("Modo: Visualização");
        view.setCamposEditaveis(false);
        view.setTabelaHabilitada(true);
        view.setFechamentoPermitido(true);
        selecaoAlterada();
    }

    @Override
    void selecaoAlterada() {
        presenter.exibirClienteSelecionado();
        boolean selecionado = presenter.getClienteSelecionado().isPresent();
        presenter.getView().setBotoesHabilitados(true, selecionado, selecionado, false, false, true);
    }

    @Override
    void novo() {
        presenter.setEstado(new ClienteInclusaoEstado(presenter));
    }

    @Override
    void editar() {
        presenter.getClienteSelecionado().ifPresent(cliente
                -> presenter.setEstado(new ClienteEdicaoEstado(presenter, cliente.getId())));
    }

    @Override
    void excluir() {
        Optional<Cliente> selecionado = presenter.getClienteSelecionado();
        if (selecionado.isEmpty()) {
            return;
        }
        Cliente cliente = selecionado.get();
        ClienteView view = presenter.getView();
        if (!view.confirmar("Confirmação de exclusão",
                "Deseja realmente excluir o cliente \"" + cliente.getNome() + "\"?")) {
            return;
        }
        try {
            presenter.getClienteService().excluir(cliente.getId());
            view.mostrarSucesso("Cliente \"" + cliente.getNome() + "\" excluído com sucesso!");
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
