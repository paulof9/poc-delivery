package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.model.PerfilUsuario;
import supermercado.model.Usuario;
import supermercado.view.UsuarioView;

class UsuarioEdicaoEstado extends UsuarioEstado {

    private final Long usuarioId;
    private final boolean administrador;

    UsuarioEdicaoEstado(UsuarioPresenter presenter, Usuario usuario) {
        super(presenter);
        this.usuarioId = usuario.getId();
        this.administrador = usuario.isAdministrador();
    }

    @Override
    void entrar() {
        UsuarioView view = presenter.getView();
        String perfilAtual = view.getPerfil();
        Long clienteAtual = presenter.getClienteSelecionadoId();
        view.setModo("Modo: Edição");
        view.setSenhas("");
        if (administrador) {
            view.setPerfis(UsuarioPresenter.descricoes(PerfilUsuario.ADMINISTRADOR));
        } else {
            view.setPerfis(UsuarioPresenter.descricoes(PerfilUsuario.ATENDENTE, PerfilUsuario.CLIENTE));
        }
        view.setPerfil(perfilAtual);
        presenter.selecionarCliente(clienteAtual);
        view.setCamposEditaveis(true);
        view.setPerfilEditavel(!administrador);
        presenter.ajustarClienteAoPerfil();
        view.setTabelaHabilitada(false);
        view.setFechamentoPermitido(false);
        view.setBotoesHabilitados(false, false, false, false, false, true, true, false);
        view.focarNome();
    }

    @Override
    void perfilAlterado() {
        presenter.ajustarClienteAoPerfil();
    }

    @Override
    void salvar() {
        UsuarioView view = presenter.getView();
        try {
            presenter.validarConfirmacaoSenha();
            presenter.getUsuarioService().alterar(presenter.getUsuarioLogado(), usuarioId,
                    view.getNomeCompleto(), view.getEmail(), view.getNomeUsuario(), view.getSenha(),
                    presenter.getPerfilSelecionado(), presenter.getClienteSelecionadoId());
            view.mostrarSucesso("Usuário salvo com sucesso!");
            presenter.carregarTabela(usuarioId);
            presenter.setEstado(new UsuarioVisualizacaoEstado(presenter));
        } catch (NegocioException e) {
            view.mostrarAviso(e.getMessage());
        }
    }

    @Override
    void cancelar() {
        presenter.setEstado(new UsuarioVisualizacaoEstado(presenter));
    }
}
