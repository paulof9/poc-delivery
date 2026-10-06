package supermercado.presenter;

abstract class UsuarioEstado {

    protected final UsuarioPresenter presenter;

    protected UsuarioEstado(UsuarioPresenter presenter) {
        this.presenter = presenter;
    }

    abstract void entrar();

    void novo() {
    }

    void editar() {
    }

    void excluir() {
    }

    void habilitar() {
    }

    void desabilitar() {
    }

    void salvar() {
    }

    void cancelar() {
    }

    void selecaoAlterada() {
    }

    void perfilAlterado() {
    }

    void clientesAlterados() {
    }

    void fechar() {
    }
}
