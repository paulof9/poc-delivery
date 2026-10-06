package supermercado.view;

public interface JanelaView {

    void fechar();

    boolean estaAberta();

    void trazerParaFrente();

    void setAcaoFechar(Runnable acao);

    void mostrarSucesso(String mensagem);

    void mostrarAviso(String mensagem);

    void mostrarErro(String mensagem);

    boolean confirmar(String titulo, String mensagem);
}
