package supermercado.presenter;

import supermercado.excecao.ValidacaoException;
import supermercado.model.Cliente;
import supermercado.model.PerfilUsuario;
import supermercado.model.Usuario;
import supermercado.servico.ClienteService;
import supermercado.servico.NotificadorAlteracoes;
import supermercado.servico.ObservadorAlteracao;
import supermercado.servico.TipoAlteracao;
import supermercado.servico.UsuarioService;
import supermercado.view.UsuarioView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class UsuarioPresenter {

    static final String SENHA_OCULTA = "********";

    private final UsuarioView view;
    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final NotificadorAlteracoes notificador;
    private final Usuario usuarioLogado;
    private final Runnable abrirInclusaoCliente;
    private final List<Long> idsUsuarios = new ArrayList<>();
    private final List<Long> idsClientes = new ArrayList<>();
    private final ObservadorAlteracao observador = this::dadosAlterados;
    private Set<Long> clientesAntesDaInclusao;
    private UsuarioEstado estado;

    public UsuarioPresenter(UsuarioView view, UsuarioService usuarioService, ClienteService clienteService,
            NotificadorAlteracoes notificador, Usuario usuarioLogado, Runnable abrirInclusaoCliente) {
        this.view = Objects.requireNonNull(view);
        this.usuarioService = Objects.requireNonNull(usuarioService);
        this.clienteService = Objects.requireNonNull(clienteService);
        this.notificador = Objects.requireNonNull(notificador);
        this.usuarioLogado = Objects.requireNonNull(usuarioLogado);
        this.abrirInclusaoCliente = Objects.requireNonNull(abrirInclusaoCliente);

        view.setAcaoNovo(() -> estado.novo());
        view.setAcaoEditar(() -> estado.editar());
        view.setAcaoExcluir(() -> estado.excluir());
        view.setAcaoHabilitar(() -> estado.habilitar());
        view.setAcaoDesabilitar(() -> estado.desabilitar());
        view.setAcaoSalvar(() -> estado.salvar());
        view.setAcaoCancelar(() -> estado.cancelar());
        view.setAcaoSelecaoAlterada(() -> estado.selecaoAlterada());
        view.setAcaoPerfilAlterado(() -> estado.perfilAlterado());
        view.setAcaoIncluirCliente(this::incluirCliente);
        view.setAcaoFechar(() -> estado.fechar());
        notificador.registrar(observador);

        estado = new UsuarioVisualizacaoEstado(this);
        carregarClientes();
        carregarTabela(null);
        estado.entrar();
    }

    UsuarioView getView() {
        return view;
    }

    UsuarioService getUsuarioService() {
        return usuarioService;
    }

    Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    void setEstado(UsuarioEstado novoEstado) {
        estado = novoEstado;
        estado.entrar();
    }

    void carregarTabela(Long idParaSelecionar) {
        idsUsuarios.clear();
        List<String[]> linhas = new ArrayList<>();
        for (Usuario usuario : usuarioService.listarTodos()) {
            idsUsuarios.add(usuario.getId());
            linhas.add(new String[]{
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getNomeUsuario(),
                usuario.getPerfil().toString(),
                usuario.getCliente() == null ? "" : usuario.getCliente().getNome(),
                usuario.getStatus().toString()
            });
        }
        view.setUsuarios(linhas);
        int linha = idsUsuarios.indexOf(idParaSelecionar);
        if (linha < 0 && !idsUsuarios.isEmpty()) {
            linha = 0;
        }
        if (linha >= 0) {
            view.selecionarLinha(linha);
        }
    }

    void carregarClientes() {
        Long selecionado = getClienteSelecionadoId();
        idsClientes.clear();
        List<String> nomes = new ArrayList<>();
        for (Cliente cliente : clienteService.listarTodos()) {
            idsClientes.add(cliente.getId());
            nomes.add(cliente.getNome());
        }
        view.setClientes(nomes);
        if (clientesAntesDaInclusao != null) {
            for (Long id : idsClientes) {
                if (!clientesAntesDaInclusao.contains(id)) {
                    selecionado = id;
                    clientesAntesDaInclusao = null;
                }
            }
        }
        selecionarCliente(selecionado);
    }

    Optional<Usuario> getUsuarioSelecionado() {
        int linha = view.getLinhaSelecionada();
        if (linha < 0 || linha >= idsUsuarios.size()) {
            return Optional.empty();
        }
        return usuarioService.buscarPorId(idsUsuarios.get(linha));
    }

    void exibirUsuarioSelecionado() {
        Optional<Usuario> selecionado = getUsuarioSelecionado();
        view.setPerfis(descricoes(PerfilUsuario.values()));
        if (selecionado.isEmpty()) {
            limparCampos();
            return;
        }
        Usuario usuario = selecionado.get();
        view.setNomeCompleto(usuario.getNomeCompleto());
        view.setEmail(usuario.getEmail());
        view.setNomeUsuario(usuario.getNomeUsuario());
        view.setSenhas(SENHA_OCULTA);
        view.setPerfil(usuario.getPerfil().toString());
        view.setStatus(usuario.getStatus().toString());
        selecionarCliente(usuario.getCliente() == null ? null : usuario.getCliente().getId());
    }

    void limparCampos() {
        view.setNomeCompleto("");
        view.setEmail("");
        view.setNomeUsuario("");
        view.setSenhas("");
        view.setStatus("");
        view.setClienteSelecionado(-1);
    }

    void selecionarCliente(Long clienteId) {
        view.setClienteSelecionado(idsClientes.indexOf(clienteId));
    }

    Long getClienteSelecionadoId() {
        int indice = view.getClienteSelecionado();
        return indice < 0 || indice >= idsClientes.size() ? null : idsClientes.get(indice);
    }

    PerfilUsuario getPerfilSelecionado() {
        String descricao = view.getPerfil();
        for (PerfilUsuario perfil : PerfilUsuario.values()) {
            if (perfil.toString().equals(descricao)) {
                return perfil;
            }
        }
        return null;
    }

    void ajustarClienteAoPerfil() {
        boolean perfilCliente = getPerfilSelecionado() == PerfilUsuario.CLIENTE;
        view.setClienteEditavel(perfilCliente);
        if (!perfilCliente) {
            view.setClienteSelecionado(-1);
        }
    }

    void validarConfirmacaoSenha() throws ValidacaoException {
        if (!view.getSenha().equals(view.getConfirmacaoSenha())) {
            throw new ValidacaoException("As senhas não conferem.");
        }
    }

    static List<String> descricoes(PerfilUsuario... perfis) {
        return Arrays.stream(perfis).map(PerfilUsuario::toString).toList();
    }

    private void incluirCliente() {
        clientesAntesDaInclusao = new HashSet<>(idsClientes);
        abrirInclusaoCliente.run();
    }

    private void dadosAlterados(TipoAlteracao tipo) {
        if (tipo == TipoAlteracao.CLIENTES) {
            carregarClientes();
            estado.clientesAlterados();
        }
    }

    void fechar() {
        notificador.remover(observador);
        view.fechar();
    }
}
