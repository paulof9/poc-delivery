package supermercado.fakes;

import supermercado.view.LoginView;
import java.util.ArrayList;
import java.util.List;

public class LoginViewFalsa implements LoginView {

    public final List<String> avisos = new ArrayList<>();
    public final List<String> erros = new ArrayList<>();
    public String identificacao = "";
    public String senha = "";
    public boolean exibida;
    public boolean fechada;
    private Runnable acaoEntrar;
    private Runnable acaoFechar;

    public void entrarCom(String identificacao, String senha) {
        this.identificacao = identificacao;
        this.senha = senha;
        acaoEntrar.run();
    }

    public void clicarFechar() {
        acaoFechar.run();
    }

    @Override
    public void exibir() {
        exibida = true;
        fechada = false;
    }

    @Override
    public void fechar() {
        fechada = true;
    }

    @Override
    public String getIdentificacao() {
        return identificacao;
    }

    @Override
    public String getSenha() {
        return senha;
    }

    @Override
    public void limparSenha() {
        senha = "";
    }

    @Override
    public void focarIdentificacao() {
    }

    @Override
    public void mostrarAviso(String mensagem) {
        avisos.add(mensagem);
    }

    @Override
    public void mostrarErro(String mensagem) {
        erros.add(mensagem);
    }

    @Override
    public void setAcaoEntrar(Runnable acao) {
        acaoEntrar = acao;
    }

    @Override
    public void setAcaoFechar(Runnable acao) {
        acaoFechar = acao;
    }
}
