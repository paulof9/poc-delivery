package supermercado.presenter;

abstract class ClienteEstado {

    protected final ClientePresenter presenter;

    protected ClienteEstado(ClientePresenter presenter) {
        this.presenter = presenter;
    }

    abstract void entrar();

    void novo() {
    }

    void editar() {
    }

    void excluir() {
    }

    void salvar() {
    }

    void cancelar() {
    }

    void selecaoAlterada() {
    }

    void fechar() {
    }
}
