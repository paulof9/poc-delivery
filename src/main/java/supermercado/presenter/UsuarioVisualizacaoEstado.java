package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.model.Usuario;
import supermercado.view.UsuarioView;
import java.util.Optional;

class UsuarioVisualizacaoEstado extends UsuarioEstado {

    UsuarioVisualizacaoEstado(UsuarioPresenter presenter) {
        super(presenter);
    }

    @Override
    void entrar() {
        UsuarioView view = presenter.getView();
        view.setModo("Modo: Visualização");
        view.setCamposEditaveis(false);
        view.setPerfilEditavel(false);
        view.setClienteEditavel(false);
        view.setTabelaHabilitada(true);
        view.setFechamentoPermitido(true);
        selecaoAlterada();
    }

    @Override
    void selecaoAlterada() {
        presenter.exibirUsuarioSelecionado();
        Optional<Usuario> selecionado = presenter.getUsuarioSelecionado();
        boolean existe = selecionado.isPresent();
        boolean gerenciavel = existe && !selecionado.get().isAdministrador();
        boolean habilitado = existe && selecionado.get().isHabilitado();
        presenter.getView().setBotoesHabilitados(true, existe, gerenciavel, gerenciavel && !habilitado,
                gerenciavel && habilitado, false, false, true);
    }

    @Override
    void clientesAlterados() {
        Optional<Usuario> selecionado = presenter.getUsuarioSelecionado();
        presenter.carregarTabela(selecionado.map(Usuario::getId).orElse(null));
        selecaoAlterada();
    }

    @Override
    void novo() {
        presenter.setEstado(new UsuarioInclusaoEstado(presenter));
    }

    @Override
    void editar() {
        presenter.getUsuarioSelecionado().ifPresent(usuario
                -> presenter.setEstado(new UsuarioEdicaoEstado(presenter, usuario)));
    }

    @Override
    void excluir() {
        Optional<Usuario> selecionado = presenter.getUsuarioSelecionado();
        if (selecionado.isEmpty()) {
            return;
        }
        Usuario usuario = selecionado.get();
        UsuarioView view = presenter.getView();
        if (!view.confirmar("Confirmação de exclusão",
                "Deseja realmente excluir o usuário \"" + usuario.getNomeUsuario() + "\"?")) {
            return;
        }
        try {
            presenter.getUsuarioService().excluir(presenter.getUsuarioLogado(), usuario.getId());
            view.mostrarSucesso("Usuário \"" + usuario.getNomeUsuario() + "\" excluído com sucesso!");
            presenter.carregarTabela(null);
            selecaoAlterada();
        } catch (NegocioException e) {
            view.mostrarAviso(e.getMessage());
        }
    }

    @Override
    void habilitar() {
        alterarStatus(true);
    }

    @Override
    void desabilitar() {
        alterarStatus(false);
    }

    private void alterarStatus(boolean habilitar) {
        Optional<Usuario> selecionado = presenter.getUsuarioSelecionado();
        if (selecionado.isEmpty()) {
            return;
        }
        Long id = selecionado.get().getId();
        try {
            if (habilitar) {
                presenter.getUsuarioService().habilitar(presenter.getUsuarioLogado(), id);
            } else {
                presenter.getUsuarioService().desabilitar(presenter.getUsuarioLogado(), id);
            }
            presenter.carregarTabela(id);
            selecaoAlterada();
        } catch (NegocioException e) {
            presenter.getView().mostrarAviso(e.getMessage());
        }
    }

    @Override
    void fechar() {
        presenter.fechar();
    }
}
