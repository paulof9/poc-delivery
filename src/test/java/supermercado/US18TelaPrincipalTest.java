package supermercado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import supermercado.excecao.NegocioException;
import supermercado.fakes.PrincipalViewFalsa;
import supermercado.model.Usuario;
import supermercado.presenter.PrincipalPresenter;
import supermercado.view.BuscaProdutoView;
import supermercado.view.CalculoMargemView;
import supermercado.view.CategoriaView;
import supermercado.view.ClienteView;
import supermercado.view.PrincipalFrame;
import supermercado.view.ProdutoFormView;
import supermercado.view.UsuarioView;

@DisplayName("US18 - Acessar a tela principal do POC Delivery")
class US18TelaPrincipalTest {

    private AmbienteDeTeste ambiente;
    private boolean telaDeAutenticacaoReaberta;

    @BeforeEach
    void carregarDados() throws NegocioException {
        ambiente = AmbienteDeTeste.comDadosIniciais();
    }

    private PrincipalViewFalsa autenticarComo(String nomeUsuario) {
        Usuario usuario = ambiente.usuario(nomeUsuario);
        PrincipalViewFalsa principal = new PrincipalViewFalsa();
        new PrincipalPresenter(principal,
                ambiente.navegador(principal, usuario, () -> telaDeAutenticacaoReaberta = true), usuario).iniciar();
        return principal;
    }

    @Test
    @DisplayName("Cenário 1: Abrir a tela principal após autenticação")
    void cenario01_abrirTelaPrincipal() {
        PrincipalViewFalsa principal = autenticarComo("admin");

        assertTrue(principal.exibida);
        assertEquals("Administrador do Sistema (Administrador)", principal.usuarioAtual);
    }

    @Test
    @DisplayName("Cenário 1: A janela principal inicia maximizada")
    void cenario01_janelaPrincipalMaximizada() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "requer ambiente gráfico");
        PrincipalFrame janela = new PrincipalFrame();
        try {
            janela.exibir();
            assertEquals(JFrame.MAXIMIZED_BOTH, janela.getExtendedState() & JFrame.MAXIMIZED_BOTH);
        } finally {
            janela.dispose();
        }
    }

    @Test
    @DisplayName("Cenário 2: Exibir as opções do menu Dados, sem ícones")
    void cenario02_opcoesDoMenuDados() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "requer ambiente gráfico");
        PrincipalFrame janela = new PrincipalFrame();
        try {
            JMenuBar barra = janela.getJMenuBar();
            List<String> menus = new ArrayList<>();
            for (int i = 0; i < barra.getMenuCount(); i++) {
                menus.add(barra.getMenu(i).getText());
            }
            assertEquals(List.of("Operação", "Dados", "Usuários"), menus);

            JMenu dados = barra.getMenu(1);
            List<String> opcoes = new ArrayList<>();
            for (int i = 0; i < dados.getItemCount(); i++) {
                opcoes.add(dados.getItem(i).getText());
            }
            assertEquals(List.of("Produtos", "Categorias", "Clientes", "Entregadores", "Taxas de entrega"), opcoes);

            for (int m = 0; m < barra.getMenuCount(); m++) {
                JMenu menu = barra.getMenu(m);
                assertNull(menu.getIcon());
                for (int i = 0; i < menu.getItemCount(); i++) {
                    JMenuItem item = menu.getItem(i);
                    assertNull(item.getIcon(), "item com ícone: " + item.getText());
                }
            }
        } finally {
            janela.dispose();
        }
    }

    @Test
    @DisplayName("Cenário 3: Abrir a tela de Clientes")
    void cenario03_abrirTelaDeClientes() {
        PrincipalViewFalsa principal = autenticarComo("admin");

        principal.acaoClientes.run();

        assertInstanceOf(ClienteView.class, principal.ultimaJanela());
    }

    @Test
    @DisplayName("Cenário 4: Abrir a tela de Usuários como Administrador")
    void cenario04_abrirTelaDeUsuariosComoAdministrador() {
        PrincipalViewFalsa principal = autenticarComo("admin");

        assertTrue(principal.manutencaoUsuariosDisponivel);
        principal.acaoUsuarios.run();

        assertInstanceOf(UsuarioView.class, principal.ultimaJanela());
    }

    @Test
    @DisplayName("Cenário 5: Restringir a manutenção de usuários para Atendente")
    void cenario05_restringirUsuariosParaAtendente() {
        PrincipalViewFalsa principal = autenticarComo("fernanda");

        assertFalse(principal.manutencaoUsuariosDisponivel);
        principal.acaoUsuarios.run();
        assertTrue(principal.janelasAbertas.isEmpty());

        assertTrue(principal.operacoesDisponiveis);
        principal.acaoClientes.run();
        assertInstanceOf(ClienteView.class, principal.ultimaJanela());
    }

    @Test
    @DisplayName("Cenário 6: Preservar os fluxos de produtos e categorias")
    void cenario06_preservarProdutosECategorias() {
        PrincipalViewFalsa principal = autenticarComo("fernanda");

        principal.acaoBuscarProdutos.run();
        assertInstanceOf(BuscaProdutoView.class, principal.ultimaJanela());
        principal.acaoIncluirProdutos.run();
        assertInstanceOf(ProdutoFormView.class, principal.ultimaJanela());
        principal.acaoCategorias.run();
        assertInstanceOf(CategoriaView.class, principal.ultimaJanela());
        principal.acaoCalcularMargem.run();
        assertInstanceOf(CalculoMargemView.class, principal.ultimaJanela());
    }

    @Test
    @DisplayName("Cenário 7: Sair da sessão")
    void cenario07_sairDaSessao() {
        PrincipalViewFalsa principal = autenticarComo("admin");

        principal.acaoSair.run();

        assertTrue(principal.sessaoFechada);
        assertTrue(telaDeAutenticacaoReaberta);
    }
}
