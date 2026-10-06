package supermercado.presenter;

abstract class CategoriaEstado {

    protected final CategoriaPresenter presenter;

    protected CategoriaEstado(CategoriaPresenter presenter) {
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
