package supermercado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import supermercado.excecao.NegocioException;
import supermercado.fakes.ClienteViewFalsa;
import supermercado.fakes.PrincipalViewFalsa;
import supermercado.model.Cliente;
import supermercado.model.TipoCliente;
import supermercado.presenter.ClientePresenter;
import supermercado.presenter.PrincipalPresenter;
import supermercado.view.ClienteFrame;
import supermercado.view.ClienteView;

@DisplayName("US19 - Manter clientes")
class US19ManterClientesTest {

    private AmbienteDeTeste ambiente;
    private ClienteViewFalsa tela;

    @BeforeEach
    void abrirTelaDeClientes() throws NegocioException {
        ambiente = AmbienteDeTeste.comDadosIniciais();
        tela = new ClienteViewFalsa();
        new ClientePresenter(tela, ambiente.clienteService);
    }

    private int quantidadeDeClientes() {
        return ambiente.clienteService.listarTodos().size();
    }

    @Test
    @DisplayName("Cenário 1: Abrir a tela de Clientes como Administrador ou Atendente")
    void cenario01_abrirTelaDeClientes() {
        for (String nomeUsuario : List.of("admin", "fernanda")) {
            PrincipalViewFalsa principal = new PrincipalViewFalsa();
            new PrincipalPresenter(principal, ambiente.navegador(principal, ambiente.usuario(nomeUsuario), () -> {
            }), ambiente.usuario(nomeUsuario));
            principal.acaoClientes.run();
            assertInstanceOf(ClienteView.class, principal.ultimaJanela());
        }

        assertEquals(10, tela.linhas.size());
        assertTrue(tela.tabelaContem("Ana Souza"));
        assertTrue(tela.tabelaContem("Patrícia Almeida"));
    }

    @Test
    @DisplayName("Cenário 2: Selecionar um cliente para visualização")
    void cenario02_selecionarClienteParaVisualizacao() {
        tela.selecionarNaTabela("Bruno Lima");

        assertEquals("Bruno Lima", tela.nome);
        assertEquals("Av. Brasil, 45", tela.logradouro);
        assertEquals("Vila do Sul", tela.bairro);
        assertEquals("Alegre", tela.cidade);
        assertEquals("ES", tela.uf);
        assertEquals("Modo: Visualização", tela.modo);
        assertFalse(tela.camposEditaveis);
        assertTrue(tela.novoHabilitado && tela.editarHabilitado && tela.excluirHabilitado && tela.fecharHabilitado);
        assertFalse(tela.salvarHabilitado || tela.cancelarHabilitado);
    }

    @Test
    @DisplayName("Cenário 3: Entrar no modo de inclusão")
    void cenario03_entrarNoModoDeInclusao() {
        tela.clicarNovo();

        assertEquals("Modo: Inclusão", tela.modo);
        assertTrue(tela.camposEditaveis);
        assertEquals("", tela.nome);
        assertEquals("PRATA", tela.tipoCliente);
        assertEquals("0,00", tela.totalCompras);
        assertTrue(tela.salvarHabilitado && tela.cancelarHabilitado);
        assertFalse(tela.tabelaHabilitada);
    }

    @Test
    @DisplayName("Cenário 4: Salvar um novo cliente")
    void cenario04_salvarNovoCliente() {
        int antes = quantidadeDeClientes();
        tela.clicarNovo();
        tela.preencher("Carla Nunes", "Rua das Palmeiras, 30", "Centro", "Alegre", "ES");

        tela.clicarSalvar();

        assertEquals(antes + 1, quantidadeDeClientes());
        assertEquals(TipoCliente.PRATA, ambiente.cliente("Carla Nunes").getTipo());
        assertTrue(tela.tabelaContem("Carla Nunes"));
        assertEquals("PRATA", tela.linhaDaTabela("Carla Nunes")[4]);
        assertEquals("Modo: Visualização", tela.modo);
    }

    @Test
    @DisplayName("Cenário 5: Cancelar a inclusão")
    void cenario05_cancelarInclusao() {
        int antes = quantidadeDeClientes();
        tela.clicarNovo();
        tela.preencher("Cliente Descartado", "Rua X, 1", "Centro", "Alegre", "ES");

        tela.clicarCancelar();

        assertEquals(antes, quantidadeDeClientes());
        assertFalse(tela.tabelaContem("Cliente Descartado"));
        assertNotEquals("Cliente Descartado", tela.nome);
        assertEquals("Modo: Visualização", tela.modo);
    }

    @Test
    @DisplayName("Cenário 6: Editar um cliente selecionado")
    void cenario06_editarClienteSelecionado() {
        tela.selecionarNaTabela("Bruno Lima");

        tela.clicarEditar();

        assertEquals("Modo: Edição", tela.modo);
        assertTrue(tela.camposEditaveis);
        assertEquals("Bruno Lima", tela.nome);
        assertEquals("PRATA", tela.tipoCliente);
        assertEquals("120,00", tela.totalCompras);
        assertTrue(tela.salvarHabilitado && tela.cancelarHabilitado);
        assertFalse(tela.novoHabilitado || tela.editarHabilitado || tela.excluirHabilitado);
    }

    @Test
    @DisplayName("Cenário 7: Salvar a edição de um cliente")
    void cenario07_salvarEdicao() {
        tela.selecionarNaTabela("Bruno Lima");
        tela.clicarEditar();
        tela.cidade = "Guaçuí";
        tela.bairro = "Centro";

        tela.clicarSalvar();

        Cliente bruno = ambiente.cliente("Bruno Lima");
        assertEquals("Guaçuí", bruno.getCidade());
        assertEquals("Centro", bruno.getBairro());
        assertEquals("Guaçuí", tela.linhaDaTabela("Bruno Lima")[1]);
        assertEquals("Modo: Visualização", tela.modo);
    }

    @Test
    @DisplayName("Cenário 8: Cancelar a edição de um cliente")
    void cenario08_cancelarEdicao() {
        tela.selecionarNaTabela("Bruno Lima");
        tela.clicarEditar();
        tela.cidade = "Vitória";

        tela.clicarCancelar();

        assertEquals("Alegre", ambiente.cliente("Bruno Lima").getCidade());
        assertEquals("Alegre", tela.cidade);
        assertEquals("Modo: Visualização", tela.modo);
        assertFalse(tela.camposEditaveis);
    }

    @Test
    @DisplayName("Cenário 9: Impedir edição manual do tipo de cliente")
    void cenario09_tipoDeClienteSomenteLeitura() {
        ClienteFrame janela = new ClienteFrame();
        new ClientePresenter(janela, ambiente.clienteService);

        assertTrue(Componentes.todos(janela, JComboBox.class).isEmpty());
        assertFalse(campoComTexto(janela, "PRATA").isEditable());

        Componentes.botao(janela, "Novo").doClick();
        assertFalse(campoComTexto(janela, "PRATA").isEditable());

        Componentes.botao(janela, "Cancelar").doClick();
        Componentes.botao(janela, "Editar").doClick();
        assertFalse(campoComTexto(janela, "PRATA").isEditable());
    }

    @Test
    @DisplayName("Cenário 10: Apresentar o total de compras como dado calculado")
    void cenario10_totalDeComprasCalculado() {
        tela.selecionarNaTabela("Ana Souza");
        assertEquals("245,80", tela.totalCompras);

        ClienteFrame janela = new ClienteFrame();
        new ClientePresenter(janela, ambiente.clienteService);
        assertFalse(campoComTexto(janela, "245,80").isEditable());
        Componentes.botao(janela, "Editar").doClick();
        assertFalse(campoComTexto(janela, "245,80").isEditable());
    }

    @Test
    @DisplayName("Cenário 11: Excluir um cliente após confirmação")
    void cenario11_excluirCliente() {
        int antes = quantidadeDeClientes();
        tela.selecionarNaTabela("Carlos Mendes");

        tela.clicarExcluir();

        assertEquals(1, tela.confirmacoesSolicitadas.size());
        assertEquals(antes - 1, quantidadeDeClientes());
        assertFalse(tela.tabelaContem("Carlos Mendes"));
    }

    @Test
    @DisplayName("Cenário 11: Não excluir quando a confirmação é recusada")
    void cenario11_naoExcluirSemConfirmacao() {
        int antes = quantidadeDeClientes();
        tela.selecionarNaTabela("Carlos Mendes");
        tela.respostaConfirmacao = false;

        tela.clicarExcluir();

        assertEquals(antes, quantidadeDeClientes());
        assertTrue(tela.tabelaContem("Carlos Mendes"));
    }

    @Test
    @DisplayName("Cenário 12: Validar dados obrigatórios na inclusão")
    void cenario12_validarDadosObrigatoriosNaInclusao() {
        int antes = quantidadeDeClientes();
        tela.clicarNovo();
        tela.preencher("", "Rua A, 10", "Centro", "Alegre", "ES");

        tela.clicarSalvar();

        assertEquals(1, tela.avisos.size());
        assertEquals(antes, quantidadeDeClientes());
        assertEquals("Modo: Inclusão", tela.modo);
    }

    @Test
    @DisplayName("Cenário 12: Validar dados inválidos na edição")
    void cenario12_validarDadosInvalidosNaEdicao() {
        tela.selecionarNaTabela("Bruno Lima");
        tela.clicarEditar();
        tela.uf = "ESP";
        tela.cidade = "Outra";

        tela.clicarSalvar();

        assertEquals(1, tela.avisos.size());
        Cliente bruno = ambiente.cliente("Bruno Lima");
        assertEquals("ES", bruno.getUf());
        assertEquals("Alegre", bruno.getCidade());
        assertEquals("Modo: Edição", tela.modo);
    }

    @Test
    @DisplayName("Cliente com usuário associado não pode ser excluído")
    void clienteComUsuarioAssociadoNaoPodeSerExcluido() {
        int antes = quantidadeDeClientes();
        tela.selecionarNaTabela("Ana Souza");

        tela.clicarExcluir();

        assertEquals(1, tela.avisos.size());
        assertEquals(antes, quantidadeDeClientes());
    }

    private static JTextField campoComTexto(ClienteFrame janela, String texto) {
        return Componentes.todos(janela, JTextField.class).stream()
                .filter(campo -> texto.equals(campo.getText()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Campo não encontrado com o texto " + texto));
    }
}
