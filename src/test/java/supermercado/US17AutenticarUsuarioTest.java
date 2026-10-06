package supermercado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import supermercado.excecao.NegocioException;
import supermercado.fakes.LoginViewFalsa;
import supermercado.model.Usuario;
import supermercado.presenter.LoginPresenter;
import supermercado.view.LoginFrame;

@DisplayName("US17 - Autenticar usuário")
class US17AutenticarUsuarioTest {

    private LoginViewFalsa tela;
    private Usuario usuarioAutenticado;
    private boolean aplicacaoEncerrada;

    @BeforeEach
    void abrirTelaDeAutenticacao() throws NegocioException {
        AmbienteDeTeste ambiente = AmbienteDeTeste.comDadosIniciais();
        tela = new LoginViewFalsa();
        new LoginPresenter(tela, ambiente.autenticacaoService, usuario -> usuarioAutenticado = usuario,
                () -> aplicacaoEncerrada = true).iniciar();
    }

    @Test
    @DisplayName("Cenário 1: Exibir a tela de autenticação ao iniciar a aplicação")
    void cenario01_exibirTelaDeAutenticacao() {
        assertTrue(tela.exibida);
        assertFalse(tela.fechada);
        assertNull(usuarioAutenticado);
    }

    @Test
    @DisplayName("Cenário 1: A janela aparece centralizada, só com Usuário ou e-mail, Senha, Entrar e Fechar")
    void cenario01_janelaCentralizadaComCamposEBotoes() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "requer ambiente gráfico");
        LoginFrame janela = new LoginFrame();
        try {
            Point centroTela = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
            int centroX = janela.getX() + janela.getWidth() / 2;
            int centroY = janela.getY() + janela.getHeight() / 2;
            assertTrue(Math.abs(centroX - centroTela.x) <= 1 && Math.abs(centroY - centroTela.y) <= 1);

            List<JTextField> campos = Componentes.todos(janela.getContentPane(), JTextField.class);
            assertEquals(2, campos.size());
            assertEquals(1, campos.stream().filter(JPasswordField.class::isInstance).count());
            assertTrue(Componentes.existeRotulo(janela.getContentPane(), "Usuário ou e-mail:"));
            assertTrue(Componentes.existeRotulo(janela.getContentPane(), "Senha:"));
            assertEquals(List.of("Entrar", "Fechar"), Componentes.textosDosBotoes(janela.getContentPane()));
            assertTrue(Componentes.todos(janela.getContentPane(), JLabel.class).stream()
                    .noneMatch(rotulo -> rotulo.getText().toLowerCase().contains("esqueceu")));
        } finally {
            janela.dispose();
        }
    }

    @Test
    @DisplayName("Cenário 2: Autenticar utilizando o nome de usuário")
    void cenario02_autenticarPorNomeDeUsuario() {
        tela.entrarCom("fernanda", "fernanda123");

        assertEquals("fernanda", usuarioAutenticado.getNomeUsuario());
        assertTrue(tela.fechada);
        assertTrue(tela.erros.isEmpty());
    }

    @Test
    @DisplayName("Cenário 3: Autenticar utilizando o e-mail")
    void cenario03_autenticarPorEmail() {
        tela.entrarCom("fernanda@pocdelivery.com", "fernanda123");

        assertEquals("fernanda", usuarioAutenticado.getNomeUsuario());
        assertTrue(tela.fechada);
    }

    @Test
    @DisplayName("Cenário 4: Rejeitar identificação inexistente")
    void cenario04_rejeitarIdentificacaoInexistente() {
        tela.entrarCom("naoexiste", "fernanda123");

        assertEquals(List.of("Credenciais inválidas."), tela.erros);
        assertNull(usuarioAutenticado);
        assertFalse(tela.fechada);
    }

    @Test
    @DisplayName("Cenário 4: Rejeitar senha incorreta")
    void cenario04_rejeitarSenhaIncorreta() {
        tela.entrarCom("fernanda", "senhaErrada");

        assertEquals(List.of("Credenciais inválidas."), tela.erros);
        assertNull(usuarioAutenticado);
        assertFalse(tela.fechada);
    }

    @Test
    @DisplayName("Cenário 5: Impedir autenticação de usuário desabilitado")
    void cenario05_impedirUsuarioDesabilitado() {
        tela.entrarCom("diego", "diego123");

        assertEquals(1, tela.erros.size());
        assertTrue(tela.erros.get(0).contains("desabilitado"));
        assertNull(usuarioAutenticado);
        assertFalse(tela.fechada);
    }

    @Test
    @DisplayName("Cenário 6: Validar campos obrigatórios")
    void cenario06_validarCamposObrigatorios() {
        tela.entrarCom("", "fernanda123");
        tela.entrarCom("fernanda", "");

        assertEquals(2, tela.avisos.size());
        assertTrue(tela.erros.isEmpty());
        assertNull(usuarioAutenticado);
        assertFalse(tela.fechada);
    }

    @Test
    @DisplayName("Cenário 7: Fechar a aplicação pela tela de autenticação")
    void cenario07_fecharAplicacao() {
        tela.clicarFechar();

        assertTrue(aplicacaoEncerrada);
        assertTrue(tela.fechada);
        assertNull(usuarioAutenticado);
    }
}
