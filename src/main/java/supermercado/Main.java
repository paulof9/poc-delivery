package supermercado;

import supermercado.excecao.NegocioException;
import supermercado.model.Usuario;
import supermercado.presenter.LoginPresenter;
import supermercado.presenter.Navegador;
import supermercado.presenter.PrincipalPresenter;
import supermercado.repositorio.CategoriaRepository;
import supermercado.repositorio.CategoriaRepositoryMemoria;
import supermercado.repositorio.ClienteRepository;
import supermercado.repositorio.ClienteRepositoryMemoria;
import supermercado.repositorio.HistoricoPrecoRepository;
import supermercado.repositorio.HistoricoPrecoRepositoryMemoria;
import supermercado.repositorio.ProdutoRepository;
import supermercado.repositorio.ProdutoRepositoryMemoria;
import supermercado.repositorio.UsuarioRepository;
import supermercado.repositorio.UsuarioRepositoryMemoria;
import supermercado.seeder.Seeder;
import supermercado.servico.AutenticacaoService;
import supermercado.servico.CalculoPrecoService;
import supermercado.servico.CategoriaService;
import supermercado.servico.ClienteService;
import supermercado.servico.HistoricoPrecoService;
import supermercado.servico.NotificadorAlteracoes;
import supermercado.servico.ProdutoService;
import supermercado.servico.UsuarioService;
import supermercado.view.LoginFrame;
import supermercado.view.PrincipalFrame;
import java.awt.EventQueue;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Main {

    private final NotificadorAlteracoes notificador = new NotificadorAlteracoes();
    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;
    private final CalculoPrecoService calculoPrecoService;
    private final HistoricoPrecoService historicoPrecoService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final AutenticacaoService autenticacaoService;

    private Main() {
        CategoriaRepository categoriaRepository = new CategoriaRepositoryMemoria();
        ProdutoRepository produtoRepository = new ProdutoRepositoryMemoria();
        HistoricoPrecoRepository historicoRepository = new HistoricoPrecoRepositoryMemoria();
        ClienteRepository clienteRepository = new ClienteRepositoryMemoria();
        UsuarioRepository usuarioRepository = new UsuarioRepositoryMemoria();

        categoriaService = new CategoriaService(categoriaRepository, produtoRepository, notificador);
        produtoService = new ProdutoService(produtoRepository, categoriaRepository, notificador);
        calculoPrecoService = new CalculoPrecoService(produtoRepository, categoriaRepository,
                historicoRepository, notificador);
        historicoPrecoService = new HistoricoPrecoService(historicoRepository);
        clienteService = new ClienteService(clienteRepository, usuarioRepository, notificador);
        usuarioService = new UsuarioService(usuarioRepository, clienteRepository, notificador);
        autenticacaoService = new AutenticacaoService(usuarioRepository);
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, erro) -> {
            erro.printStackTrace();
            EventQueue.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Ocorreu um erro inesperado:\n" + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE));
        });

        Main aplicacao = new Main();
        try {
            new Seeder(aplicacao.categoriaService, aplicacao.produtoService, aplicacao.calculoPrecoService,
                    aplicacao.clienteService, aplicacao.usuarioService).executar();
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(null, "Falha na carga inicial de dados:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        EventQueue.invokeLater(() -> {
            configurarAparencia();
            aplicacao.abrirLogin();
        });
    }

    private void abrirLogin() {
        new LoginPresenter(new LoginFrame(), autenticacaoService, this::abrirPrincipal, () -> System.exit(0))
                .iniciar();
    }

    private void abrirPrincipal(Usuario usuario) {
        PrincipalFrame principal = new PrincipalFrame();
        Navegador navegador = new Navegador(principal, categoriaService, produtoService, calculoPrecoService,
                historicoPrecoService, clienteService, usuarioService, notificador, usuario, this::abrirLogin);
        new PrincipalPresenter(principal, navegador, usuario).iniciar();
    }

    private static void configurarAparencia() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            System.err.println("Aparência nativa indisponível; usando a padrão do Java: " + e.getMessage());
        }
    }
}
