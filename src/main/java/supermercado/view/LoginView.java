package supermercado.view;

public interface LoginView {

    void exibir();

    void fechar();

    String getIdentificacao();

    String getSenha();

    void limparSenha();

    void focarIdentificacao();

    void mostrarAviso(String mensagem);

    void mostrarErro(String mensagem);

    void setAcaoEntrar(Runnable acao);

    void setAcaoFechar(Runnable acao);
}
