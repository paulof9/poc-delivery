package supermercado.presenter;

import supermercado.excecao.RegraNegocioException;
import supermercado.excecao.ValidacaoException;
import supermercado.model.Usuario;
import supermercado.servico.AutenticacaoService;
import supermercado.view.LoginView;
import java.util.Objects;
import java.util.function.Consumer;

public class LoginPresenter {

    private final LoginView view;
    private final AutenticacaoService autenticacaoService;
    private final Consumer<Usuario> aoAutenticar;
    private final Runnable encerrarAplicacao;

    public LoginPresenter(LoginView view, AutenticacaoService autenticacaoService, Consumer<Usuario> aoAutenticar,
            Runnable encerrarAplicacao) {
        this.view = Objects.requireNonNull(view);
        this.autenticacaoService = Objects.requireNonNull(autenticacaoService);
        this.aoAutenticar = Objects.requireNonNull(aoAutenticar);
        this.encerrarAplicacao = Objects.requireNonNull(encerrarAplicacao);

        view.setAcaoEntrar(this::entrar);
        view.setAcaoFechar(this::fechar);
    }

    public void iniciar() {
        view.exibir();
    }

    private void entrar() {
        try {
            Usuario usuario = autenticacaoService.autenticar(view.getIdentificacao(), view.getSenha());
            view.fechar();
            aoAutenticar.accept(usuario);
        } catch (ValidacaoException e) {
            view.mostrarAviso(e.getMessage());
            view.focarIdentificacao();
        } catch (RegraNegocioException e) {
            view.mostrarErro(e.getMessage());
            view.limparSenha();
        }
    }

    private void fechar() {
        view.fechar();
        encerrarAplicacao.run();
    }
}
