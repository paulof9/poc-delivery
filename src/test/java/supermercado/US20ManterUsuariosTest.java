package supermercado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import supermercado.excecao.NegocioException;
import supermercado.excecao.RegraNegocioException;
import supermercado.fakes.PrincipalViewFalsa;
import supermercado.fakes.UsuarioViewFalsa;
import supermercado.model.PerfilUsuario;
import supermercado.model.StatusUsuario;
import supermercado.model.Usuario;
import supermercado.presenter.Navegador;
import supermercado.presenter.PrincipalPresenter;
import supermercado.presenter.UsuarioPresenter;
import supermercado.seeder.Seeder;
import supermercado.view.CalculoMargemView;
import supermercado.view.CategoriaView;
import supermercado.view.ClienteFrame;
import supermercado.view.ClienteView;
import supermercado.view.BuscaProdutoView;
import supermercado.view.UsuarioFrame;
import supermercado.view.UsuarioView;

@DisplayName("US20 - Manter usuários")
class US20ManterUsuariosTest {

    private AmbienteDeTeste ambiente;
    private Usuario administrador;
    private UsuarioViewFalsa tela;
    private boolean inclusaoDeClienteSolicitada;

    @BeforeEach
    void abrirTelaDeUsuarios() throws NegocioException {
        ambiente = AmbienteDeTeste.comDadosIniciais();
        administrador = ambiente.usuario("admin");
        tela = new UsuarioViewFalsa();
        new UsuarioPresenter(tela, ambiente.usuarioService, ambiente.clienteService, ambiente.notificador,
                administrador, () -> inclusaoDeClienteSolicitada = true);
    }

    private int quantidadeDeUsuarios() {
        return ambiente.usuarioService.listarTodos().size();
    }

    private boolean existeUsuario(String nomeUsuario) {
        return ambiente.usuarioService.listarTodos().stream()
                .anyMatch(usuario -> usuario.getNomeUsuario().equals(nomeUsuario));
    }

    private PrincipalViewFalsa principalDe(String nomeUsuario) {
        Usuario usuario = ambiente.usuario(nomeUsuario);
        PrincipalViewFalsa principal = new PrincipalViewFalsa();
        new PrincipalPresenter(principal, ambiente.navegador(principal, usuario, () -> {
        }), usuario).iniciar();
        return principal;
    }

    @Test
    @DisplayName("Cenário 1: Disponibilizar o primeiro usuário Administrador")
    void cenario01_primeiroUsuarioAdministrador() throws NegocioException {
        AmbienteDeTeste vazio = new AmbienteDeTeste();
        assertTrue(vazio.usuarioService.listarTodos().isEmpty());

        new Seeder(vazio.categoriaService, vazio.produtoService, vazio.calculoPrecoService,
                vazio.clienteService, vazio.usuarioService).executar();

        Usuario primeiro = vazio.usuarioService.listarTodos().get(0);
        assertEquals(PerfilUsuario.ADMINISTRADOR, primeiro.getPerfil());
        assertTrue(primeiro.isHabilitado());
        assertNull(primeiro.getCliente());
        assertThrows(RegraNegocioException.class, () -> vazio.usuarioService.incluirAdministrador(
                "Outro Admin", "outro@pocdelivery.com", "outroadmin", "senha123"));
    }

    @Test
    @DisplayName("Cenário 2: Abrir a tela de Usuários como Administrador")
    void cenario02_abrirTelaDeUsuarios() {
        PrincipalViewFalsa principal = principalDe("admin");

        principal.acaoUsuarios.run();

        assertInstanceOf(UsuarioView.class, principal.ultimaJanela());
        assertEquals(quantidadeDeUsuarios(), tela.linhas.size());
        assertTrue(tela.tabelaContem("admin"));
        assertTrue(tela.tabelaContem("fernanda"));
    }

    @Test
    @DisplayName("Cenário 3: Impedir acesso do Atendente à manutenção de usuários")
    void cenario03_impedirAcessoDoAtendente() {
        PrincipalViewFalsa principal = principalDe("fernanda");

        assertFalse(principal.manutencaoUsuariosDisponivel);
        principal.acaoUsuarios.run();
        assertTrue(principal.janelasAbertas.isEmpty());

        Usuario atendente = ambiente.usuario("fernanda");
        assertThrows(RegraNegocioException.class, () -> ambiente.usuarioService.incluir(atendente,
                "Novo Atendente", "novo@pocdelivery.com", "novo", "senha123", PerfilUsuario.ATENDENTE, null));
        assertThrows(RegraNegocioException.class,
                () -> ambiente.usuarioService.desabilitar(atendente, ambiente.usuario("ricardo").getId()));
    }

    @Test
    @DisplayName("Cenário 4: Selecionar um usuário para visualização")
    void cenario04_selecionarUsuarioParaVisualizacao() {
        tela.selecionarNaTabela("fernanda");

        assertEquals("Fernanda Alves", tela.nomeCompleto);
        assertEquals("fernanda@pocdelivery.com", tela.email);
        assertEquals("fernanda", tela.nomeUsuario);
        assertEquals("Atendente", tela.perfil);
        assertEquals("Habilitado", tela.status);
        assertNotEquals("fernanda123", tela.senha);
        assertEquals("Modo: Visualização", tela.modo);
        assertFalse(tela.camposEditaveis || tela.perfilEditavel || tela.clienteEditavel);
    }

    @Test
    @DisplayName("Cenário 5: Entrar no modo de inclusão de usuário")
    void cenario05_entrarNoModoDeInclusao() {
        tela.clicarNovo();

        assertEquals("Modo: Inclusão", tela.modo);
        assertTrue(tela.camposEditaveis);
        assertTrue(tela.perfilEditavel);
        assertEquals(List.of("Atendente", "Cliente"), tela.perfis);
        assertEquals("", tela.nomeCompleto);
        assertEquals("", tela.senha);
        assertEquals("Habilitado", tela.status);
        assertTrue(tela.salvarHabilitado && tela.cancelarHabilitado);
        assertFalse(tela.habilitarHabilitado || tela.desabilitarHabilitado);
    }

    @Test
    @DisplayName("Cenário 6: Incluir usuário Cliente associado a cliente existente")
    void cenario06_incluirUsuarioClienteAssociado() {
        tela.clicarNovo();
        tela.escolherPerfil("Cliente");

        assertTrue(tela.clienteEditavel);
        assertEquals(ambiente.clienteService.listarTodos().size(), tela.clientes.size());
        assertTrue(tela.clientes.contains("Carlos Mendes"));

        tela.escolherCliente("Carlos Mendes");
        tela.preencher("Carlos Mendes", "carlos@cliente.com", "carlosmendes", "carlos123", "carlos123");
        tela.clicarSalvar();

        Usuario carlos = ambiente.usuario("carlosmendes");
        assertEquals(PerfilUsuario.CLIENTE, carlos.getPerfil());
        assertEquals("Carlos Mendes", carlos.getCliente().getNome());
        assertEquals("Carlos Mendes", tela.linhaDaTabela("carlosmendes")[4]);
    }

    @Test
    @DisplayName("Cenário 7: Exigir cliente associado na inclusão de usuário Cliente")
    void cenario07_exigirClienteNaInclusao() {
        int antes = quantidadeDeUsuarios();
        tela.clicarNovo();
        tela.escolherPerfil("Cliente");
        tela.preencher("Sem Cliente", "sem@cliente.com", "semcliente", "senha123", "senha123");

        tela.clicarSalvar();

        assertEquals(1, tela.avisos.size());
        assertEquals(antes, quantidadeDeUsuarios());
        assertEquals("Modo: Inclusão", tela.modo);
    }

    @Test
    @DisplayName("Cenário 7: Exigir cliente associado na edição de usuário Cliente")
    void cenario07_exigirClienteNaEdicao() {
        tela.selecionarNaTabela("anasouza");
        tela.clicarEditar();
        tela.setClienteSelecionado(-1);

        tela.clicarSalvar();

        assertEquals(1, tela.avisos.size());
        assertEquals("Ana Souza", ambiente.usuario("anasouza").getCliente().getNome());
    }

    @Test
    @DisplayName("Cenário 8: Incluir cliente durante o cadastro de usuário")
    void cenario08_incluirClienteDuranteCadastro() throws NegocioException {
        PrincipalViewFalsa principal = new PrincipalViewFalsa();
        Navegador navegador = ambiente.navegador(principal, administrador, () -> {
        });
        UsuarioViewFalsa telaUsuarios = new UsuarioViewFalsa();
        new UsuarioPresenter(telaUsuarios, ambiente.usuarioService, ambiente.clienteService, ambiente.notificador,
                administrador, navegador::abrirInclusaoCliente);
        telaUsuarios.clicarNovo();
        telaUsuarios.escolherPerfil("Cliente");
        assertFalse(telaUsuarios.clientes.contains("Roberta Dias"));

        telaUsuarios.clicarIncluirCliente();

        ClienteFrame telaClientes = assertInstanceOf(ClienteFrame.class, principal.ultimaJanela());
        assertTrue(Componentes.existeRotulo(telaClientes, "Modo: Inclusão"));

        ambiente.clienteService.incluir("Roberta Dias", "Rua do Sol, 5", "Centro", "Alegre", "ES");

        assertTrue(telaUsuarios.clientes.contains("Roberta Dias"));
        assertEquals("Roberta Dias", telaUsuarios.clienteEscolhido());
        assertEquals("Modo: Inclusão", telaUsuarios.modo);
    }

    @Test
    @DisplayName("Cenário 8: O botão Incluir cliente aciona a abertura da tela de Clientes")
    void cenario08_botaoIncluirClienteAbreTelaDeClientes() {
        tela.clicarNovo();
        tela.escolherPerfil("Cliente");

        tela.clicarIncluirCliente();

        assertTrue(inclusaoDeClienteSolicitada);
    }

    @Test
    @DisplayName("Cenário 9: Incluir usuário Atendente")
    void cenario09_incluirUsuarioAtendente() {
        tela.clicarNovo();
        tela.escolherPerfil("Atendente");

        assertFalse(tela.clienteEditavel);
        assertNull(tela.clienteEscolhido());

        tela.preencher("Juliana Pereira", "juliana@pocdelivery.com", "juliana", "juliana123", "juliana123");
        tela.clicarSalvar();

        Usuario juliana = ambiente.usuario("juliana");
        assertEquals(PerfilUsuario.ATENDENTE, juliana.getPerfil());
        assertNull(juliana.getCliente());
        assertTrue(tela.avisos.isEmpty());
    }

    @Test
    @DisplayName("Cenário 10: Validar a confirmação de senha na inclusão")
    void cenario10_validarConfirmacaoNaInclusao() {
        tela.clicarNovo();
        tela.escolherPerfil("Atendente");
        tela.preencher("Juliana Pereira", "juliana@pocdelivery.com", "juliana", "juliana123", "outrasenha");

        tela.clicarSalvar();

        assertEquals(List.of("As senhas não conferem."), tela.avisos);
        assertFalse(existeUsuario("juliana"));
    }

    @Test
    @DisplayName("Cenário 10: Validar a confirmação de senha ao alterar a senha")
    void cenario10_validarConfirmacaoNaAlteracao() throws NegocioException {
        tela.selecionarNaTabela("fernanda");
        tela.clicarEditar();
        tela.senha = "novaSenha1";
        tela.confirmacaoSenha = "novaSenha2";

        tela.clicarSalvar();

        assertEquals(List.of("As senhas não conferem."), tela.avisos);
        assertEquals("fernanda", ambiente.autenticacaoService.autenticar("fernanda", "fernanda123").getNomeUsuario());
    }

    @Test
    @DisplayName("Cenário 11: Mostrar e ocultar senha durante entrada de dados")
    void cenario11_mostrarEOcultarSenha() {
        UsuarioFrame janela = new UsuarioFrame();
        new UsuarioPresenter(janela, ambiente.usuarioService, ambiente.clienteService, ambiente.notificador,
                administrador, () -> {
                });
        List<JButton> botoesMostrar = Componentes.todos(janela, JButton.class).stream()
                .filter(botao -> "Mostrar".equals(botao.getText()))
                .toList();
        assertEquals(2, botoesMostrar.size());
        assertTrue(botoesMostrar.stream().noneMatch(JButton::isEnabled));

        Componentes.botao(janela, "Novo").doClick();
        JPasswordField senha = Componentes.todos(janela, JPasswordField.class).get(0);
        char mascara = senha.getEchoChar();
        senha.setText("segredo1");
        JButton mostrar = botoesMostrar.get(0);

        mostrar.doClick();
        assertEquals(0, senha.getEchoChar());
        assertEquals("Ocultar", mostrar.getText());

        mostrar.doClick();
        assertEquals(mascara, senha.getEchoChar());
        assertNotEquals(0, mascara);
    }

    @Test
    @DisplayName("Cenário 12: Editar um usuário não administrador")
    void cenario12_editarUsuarioNaoAdministrador() {
        tela.selecionarNaTabela("anasouza");

        tela.clicarEditar();

        assertEquals("Modo: Edição", tela.modo);
        assertTrue(tela.camposEditaveis);
        assertTrue(tela.perfilEditavel);
        assertTrue(tela.clienteEditavel);
        assertEquals("Ana Souza", tela.clienteEscolhido());
        assertTrue(tela.salvarHabilitado && tela.cancelarHabilitado);

        tela.nomeCompleto = "Ana Souza Lima";
        tela.clicarSalvar();
        assertEquals("Ana Souza Lima", ambiente.usuario("anasouza").getNomeCompleto());
        assertTrue(tela.avisos.isEmpty());
    }

    @Test
    @DisplayName("Cenário 12: Na edição de Atendente o cliente associado fica indisponível")
    void cenario12_editarAtendenteSemCliente() {
        tela.selecionarNaTabela("fernanda");

        tela.clicarEditar();

        assertTrue(tela.perfilEditavel);
        assertFalse(tela.clienteEditavel);
    }

    @Test
    @DisplayName("Cenário 13: Cancelar a edição de usuário")
    void cenario13_cancelarEdicao() {
        tela.selecionarNaTabela("fernanda");
        tela.clicarEditar();
        tela.nomeCompleto = "Nome Alterado";
        tela.email = "alterado@pocdelivery.com";

        tela.clicarCancelar();

        assertEquals("Fernanda Alves", tela.nomeCompleto);
        assertEquals("fernanda@pocdelivery.com", tela.email);
        assertEquals("Fernanda Alves", ambiente.usuario("fernanda").getNomeCompleto());
        assertEquals("Modo: Visualização", tela.modo);
    }

    @Test
    @DisplayName("Cenário 14: Desabilitar usuário não administrador")
    void cenario14_desabilitarUsuario() {
        tela.selecionarNaTabela("fernanda");
        assertTrue(tela.desabilitarHabilitado);
        assertFalse(tela.habilitarHabilitado);

        tela.clicarDesabilitar();

        assertEquals(StatusUsuario.DESABILITADO, ambiente.usuario("fernanda").getStatus());
        assertEquals("Desabilitado", tela.linhaDaTabela("fernanda")[5]);
        assertEquals("Desabilitado", tela.status);
    }

    @Test
    @DisplayName("Cenário 14 e 15: O status é apresentado na tabela sem codificação por cores")
    void cenario14e15_statusSemCores() {
        UsuarioFrame janela = new UsuarioFrame();
        new UsuarioPresenter(janela, ambiente.usuarioService, ambiente.clienteService, ambiente.notificador,
                administrador, () -> {
                });
        JTable tabela = Componentes.todos(janela, JTable.class).get(0);
        tabela.clearSelection();
        int colunaStatus = 5;

        Component habilitado = celula(tabela, linhaComStatus(tabela, "Habilitado"), colunaStatus);
        Color frenteHabilitado = habilitado.getForeground();
        Color fundoHabilitado = habilitado.getBackground();
        Component desabilitado = celula(tabela, linhaComStatus(tabela, "Desabilitado"), colunaStatus);

        assertEquals(frenteHabilitado, desabilitado.getForeground());
        assertEquals(fundoHabilitado, desabilitado.getBackground());
    }

    @Test
    @DisplayName("Cenário 15: Habilitar usuário não administrador")
    void cenario15_habilitarUsuario() {
        tela.selecionarNaTabela("diego");
        assertTrue(tela.habilitarHabilitado);
        assertFalse(tela.desabilitarHabilitado);

        tela.clicarHabilitar();

        assertEquals(StatusUsuario.HABILITADO, ambiente.usuario("diego").getStatus());
        assertEquals("Habilitado", tela.linhaDaTabela("diego")[5]);
    }

    @Test
    @DisplayName("Cenário 16: Impedir que o Administrador seja desabilitado")
    void cenario16_administradorNaoPodeSerDesabilitado() {
        tela.selecionarNaTabela("admin");

        assertFalse(tela.habilitarHabilitado);
        assertFalse(tela.desabilitarHabilitado);
        assertThrows(RegraNegocioException.class,
                () -> ambiente.usuarioService.desabilitar(administrador, administrador.getId()));
    }

    @Test
    @DisplayName("Cenário 17: Impedir exclusão do Administrador")
    void cenario17_administradorNaoPodeSerExcluido() {
        tela.selecionarNaTabela("admin");

        assertFalse(tela.excluirHabilitado);
        assertThrows(RegraNegocioException.class,
                () -> ambiente.usuarioService.excluir(administrador, administrador.getId()));
    }

    @Test
    @DisplayName("Cenário 18: Excluir usuário não administrador sem excluir cliente associado")
    void cenario18_excluirUsuarioMantendoCliente() {
        int clientesAntes = ambiente.clienteService.listarTodos().size();
        tela.selecionarNaTabela("anasouza");

        tela.clicarExcluir();

        assertEquals(1, tela.confirmacoesSolicitadas.size());
        assertFalse(existeUsuario("anasouza"));
        assertFalse(tela.tabelaContem("anasouza"));
        assertEquals(clientesAntes, ambiente.clienteService.listarTodos().size());
        assertEquals("Ana Souza", ambiente.cliente("Ana Souza").getNome());
    }

    @Test
    @DisplayName("Cenário 19: Validar unicidade do nome de usuário na inclusão")
    void cenario19_nomeDeUsuarioRepetidoNaInclusao() {
        int antes = quantidadeDeUsuarios();
        tela.clicarNovo();
        tela.escolherPerfil("Atendente");
        tela.preencher("Outra Fernanda", "outra@pocdelivery.com", "fernanda", "senha123", "senha123");

        tela.clicarSalvar();

        assertEquals(1, tela.avisos.size());
        assertEquals(antes, quantidadeDeUsuarios());
    }

    @Test
    @DisplayName("Cenário 19: Validar unicidade do nome de usuário na edição")
    void cenario19_nomeDeUsuarioRepetidoNaEdicao() {
        tela.selecionarNaTabela("ricardo");
        tela.clicarEditar();
        tela.nomeUsuario = "FERNANDA";

        tela.clicarSalvar();

        assertEquals(1, tela.avisos.size());
        assertEquals("ricardo", ambiente.usuario("ricardo").getNomeUsuario());
    }

    @Test
    @DisplayName("Cenário 20: Preservar o acesso do Administrador às funcionalidades do Atendente")
    void cenario20_administradorTemAcessoDoAtendente() {
        PrincipalViewFalsa principal = principalDe("admin");

        assertTrue(principal.operacoesDisponiveis);
        principal.acaoClientes.run();
        assertInstanceOf(ClienteView.class, principal.ultimaJanela());
        principal.acaoBuscarProdutos.run();
        assertInstanceOf(BuscaProdutoView.class, principal.ultimaJanela());
        principal.acaoCategorias.run();
        assertInstanceOf(CategoriaView.class, principal.ultimaJanela());
        principal.acaoCalcularMargem.run();
        assertInstanceOf(CalculoMargemView.class, principal.ultimaJanela());
    }

    private static int linhaComStatus(JTable tabela, String status) {
        for (int linha = 0; linha < tabela.getRowCount(); linha++) {
            if (status.equals(tabela.getValueAt(linha, 5))) {
                return linha;
            }
        }
        throw new AssertionError("Nenhuma linha com status " + status);
    }

    private static Component celula(JTable tabela, int linha, int coluna) {
        return tabela.prepareRenderer(tabela.getCellRenderer(linha, coluna), linha, coluna);
    }
}
