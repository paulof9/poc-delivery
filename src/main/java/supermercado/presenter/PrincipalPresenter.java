package supermercado.presenter;

import supermercado.model.PerfilUsuario;
import supermercado.model.Usuario;
import supermercado.view.PrincipalView;
import java.util.Objects;

public class PrincipalPresenter {

    private final PrincipalView view;

    public PrincipalPresenter(PrincipalView view, Navegador navegador, Usuario usuario) {
        this.view = Objects.requireNonNull(view);
        Objects.requireNonNull(navegador);
        Objects.requireNonNull(usuario);

        view.setAcaoIncluirProdutos(navegador::abrirInclusaoProduto);
        view.setAcaoBuscarProdutos(navegador::abrirBuscaProdutos);
        view.setAcaoCategorias(navegador::abrirCategorias);
        view.setAcaoCalcularMargem(navegador::abrirCalculoMargem);
        view.setAcaoClientes(navegador::abrirClientes);
        view.setAcaoUsuarios(navegador::abrirUsuarios);
        view.setAcaoSair(navegador::sair);

        view.setUsuarioAtual(usuario.getNomeCompleto() + " (" + usuario.getPerfil() + ")");
        view.setOperacoesDisponiveis(usuario.getPerfil() != PerfilUsuario.CLIENTE);
        view.setManutencaoUsuariosDisponivel(usuario.isAdministrador());
    }

    public void iniciar() {
        view.exibir();
    }
}
