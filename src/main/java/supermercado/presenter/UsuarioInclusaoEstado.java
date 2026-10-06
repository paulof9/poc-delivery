package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.model.PerfilUsuario;
import supermercado.model.StatusUsuario;
import supermercado.model.Usuario;
import supermercado.view.UsuarioView;

class UsuarioInclusaoEstado extends UsuarioEstado {

    UsuarioInclusaoEstado(UsuarioPresenter presenter) {
        super(presenter);
    }

    @Override
    void entrar() {
        UsuarioView view = presenter.getView();
        view.setModo("Modo: Inclusão");
        presenter.limparCampos();
        view.setPerfis(UsuarioPresenter.descricoes(PerfilUsuario.ATENDENTE, PerfilUsuario.CLIENTE));
        view.setPerfil(PerfilUsuario.CLIENTE.toString());
        view.setStatus(StatusUsuario.HABILITADO.toString());
        view.setCamposEditaveis(true);
        view.setPerfilEditavel(true);
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
            Usuario novo = presenter.getUsuarioService().incluir(presenter.getUsuarioLogado(),
                    view.getNomeCompleto(), view.getEmail(), view.getNomeUsuario(), view.getSenha(),
                    presenter.getPerfilSelecionado(), presenter.getClienteSelecionadoId());
            view.mostrarSucesso("Usuário salvo com sucesso!");
            presenter.carregarTabela(novo.getId());
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
