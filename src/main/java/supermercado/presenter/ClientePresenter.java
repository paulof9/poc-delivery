package supermercado.presenter;

import supermercado.model.Cliente;
import supermercado.servico.ClienteService;
import supermercado.view.ClienteView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClientePresenter {

    private final ClienteView view;
    private final ClienteService clienteService;
    private final List<Long> idsClientes = new ArrayList<>();
    private ClienteEstado estado;

    public ClientePresenter(ClienteView view, ClienteService clienteService) {
        this.view = Objects.requireNonNull(view);
        this.clienteService = Objects.requireNonNull(clienteService);

        view.setAcaoNovo(() -> estado.novo());
        view.setAcaoEditar(() -> estado.editar());
        view.setAcaoExcluir(() -> estado.excluir());
        view.setAcaoSalvar(() -> estado.salvar());
        view.setAcaoCancelar(() -> estado.cancelar());
        view.setAcaoSelecaoAlterada(() -> estado.selecaoAlterada());
        view.setAcaoFechar(() -> estado.fechar());

        estado = new ClienteVisualizacaoEstado(this);
        carregarTabela(null);
        estado.entrar();
    }

    public void iniciarInclusao() {
        estado.novo();
    }

    ClienteView getView() {
        return view;
    }

    ClienteService getClienteService() {
        return clienteService;
    }

    void setEstado(ClienteEstado novoEstado) {
        estado = novoEstado;
        estado.entrar();
    }

    void carregarTabela(Long idParaSelecionar) {
        idsClientes.clear();
        List<String[]> linhas = new ArrayList<>();
        for (Cliente cliente : clienteService.listarTodos()) {
            idsClientes.add(cliente.getId());
            linhas.add(new String[]{
                cliente.getNome(),
                cliente.getCidade(),
                cliente.getUf(),
                cliente.getBairro(),
                cliente.getTipo().name(),
                Formatador.formatarDecimal(cliente.getTotalCompras())
            });
        }
        view.setClientes(linhas);
        int linha = idsClientes.indexOf(idParaSelecionar);
        if (linha < 0 && !idsClientes.isEmpty()) {
            linha = 0;
        }
        if (linha >= 0) {
            view.selecionarLinha(linha);
        }
    }

    Optional<Cliente> getClienteSelecionado() {
        int linha = view.getLinhaSelecionada();
        if (linha < 0 || linha >= idsClientes.size()) {
            return Optional.empty();
        }
        return clienteService.buscarPorId(idsClientes.get(linha));
    }

    void exibirClienteSelecionado() {
        Optional<Cliente> selecionado = getClienteSelecionado();
        if (selecionado.isPresent()) {
            Cliente cliente = selecionado.get();
            view.setNome(cliente.getNome());
            view.setLogradouro(cliente.getLogradouro());
            view.setBairro(cliente.getBairro());
            view.setCidade(cliente.getCidade());
            view.setUf(cliente.getUf());
            view.setTipoCliente(cliente.getTipo().name());
            view.setTotalCompras(Formatador.formatarDecimal(cliente.getTotalCompras()));
        } else {
            limparCampos();
        }
    }

    void limparCampos() {
        view.setNome("");
        view.setLogradouro("");
        view.setBairro("");
        view.setCidade("");
        view.setUf("");
        view.setTipoCliente("");
        view.setTotalCompras("");
    }

    void fechar() {
        view.fechar();
    }
}
