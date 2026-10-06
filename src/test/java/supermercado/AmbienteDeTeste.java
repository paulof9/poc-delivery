package supermercado;

import supermercado.excecao.NegocioException;
import supermercado.model.Cliente;
import supermercado.model.Usuario;
import supermercado.presenter.Navegador;
import supermercado.repositorio.CategoriaRepository;
import supermercado.repositorio.CategoriaRepositoryMemoria;
import supermercado.repositorio.ClienteRepositoryMemoria;
import supermercado.repositorio.HistoricoPrecoRepository;
import supermercado.repositorio.HistoricoPrecoRepositoryMemoria;
import supermercado.repositorio.ProdutoRepository;
import supermercado.repositorio.ProdutoRepositoryMemoria;
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
import supermercado.view.PrincipalView;

public class AmbienteDeTeste {

    public final NotificadorAlteracoes notificador = new NotificadorAlteracoes();
    public final CategoriaService categoriaService;
    public final ProdutoService produtoService;
    public final CalculoPrecoService calculoPrecoService;
    public final HistoricoPrecoService historicoPrecoService;
    public final ClienteService clienteService;
    public final UsuarioService usuarioService;
    public final AutenticacaoService autenticacaoService;

    public AmbienteDeTeste() {
        CategoriaRepository categoriaRepository = new CategoriaRepositoryMemoria();
        ProdutoRepository produtoRepository = new ProdutoRepositoryMemoria();
        HistoricoPrecoRepository historicoRepository = new HistoricoPrecoRepositoryMemoria();
        ClienteRepositoryMemoria clienteRepository = new ClienteRepositoryMemoria();
        UsuarioRepositoryMemoria usuarioRepository = new UsuarioRepositoryMemoria();

        categoriaService = new CategoriaService(categoriaRepository, produtoRepository, notificador);
        produtoService = new ProdutoService(produtoRepository, categoriaRepository, notificador);
        calculoPrecoService = new CalculoPrecoService(produtoRepository, categoriaRepository,
                historicoRepository, notificador);
        historicoPrecoService = new HistoricoPrecoService(historicoRepository);
        clienteService = new ClienteService(clienteRepository, usuarioRepository, notificador);
        usuarioService = new UsuarioService(usuarioRepository, clienteRepository, notificador);
        autenticacaoService = new AutenticacaoService(usuarioRepository);
    }

    public static AmbienteDeTeste comDadosIniciais() throws NegocioException {
        AmbienteDeTeste ambiente = new AmbienteDeTeste();
        new Seeder(ambiente.categoriaService, ambiente.produtoService, ambiente.calculoPrecoService,
                ambiente.clienteService, ambiente.usuarioService).executar();
        return ambiente;
    }

    public Usuario usuario(String nomeUsuario) {
        return usuarioService.listarTodos().stream()
                .filter(usuario -> usuario.getNomeUsuario().equals(nomeUsuario))
                .findFirst()
                .orElseThrow();
    }

    public Cliente cliente(String nome) {
        return clienteService.listarTodos().stream()
                .filter(cliente -> cliente.getNome().equals(nome))
                .findFirst()
                .orElseThrow();
    }

    public Navegador navegador(PrincipalView principal, Usuario usuarioLogado, Runnable aoSair) {
        return new Navegador(principal, categoriaService, produtoService, calculoPrecoService,
                historicoPrecoService, clienteService, usuarioService, notificador, usuarioLogado, aoSair);
    }
}
