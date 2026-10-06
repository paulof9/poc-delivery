package supermercado.presenter;

import supermercado.model.Usuario;
import supermercado.servico.CalculoPrecoService;
import supermercado.servico.CategoriaService;
import supermercado.servico.ClienteService;
import supermercado.servico.HistoricoPrecoService;
import supermercado.servico.NotificadorAlteracoes;
import supermercado.servico.ProdutoService;
import supermercado.servico.UsuarioService;
import supermercado.view.BuscaProdutoFrame;
import supermercado.view.CalculoMargemFrame;
import supermercado.view.CategoriaFrame;
import supermercado.view.ClienteFrame;
import supermercado.view.HistoricoPrecoFrame;
import supermercado.view.JanelaView;
import supermercado.view.PrincipalView;
import supermercado.view.ProdutoFormFrame;
import supermercado.view.ProdutoVisualizacaoFrame;
import supermercado.view.UsuarioFrame;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class Navegador {

    private final PrincipalView principal;
    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;
    private final CalculoPrecoService calculoPrecoService;
    private final HistoricoPrecoService historicoPrecoService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final NotificadorAlteracoes notificador;
    private final Usuario usuarioLogado;
    private final Runnable aoSair;
    private final Map<String, JanelaView> janelas = new HashMap<>();
    private ClientePresenter clientePresenter;

    public Navegador(PrincipalView principal, CategoriaService categoriaService, ProdutoService produtoService,
            CalculoPrecoService calculoPrecoService, HistoricoPrecoService historicoPrecoService,
            ClienteService clienteService, UsuarioService usuarioService, NotificadorAlteracoes notificador,
            Usuario usuarioLogado, Runnable aoSair) {
        this.principal = Objects.requireNonNull(principal);
        this.categoriaService = Objects.requireNonNull(categoriaService);
        this.produtoService = Objects.requireNonNull(produtoService);
        this.calculoPrecoService = Objects.requireNonNull(calculoPrecoService);
        this.historicoPrecoService = Objects.requireNonNull(historicoPrecoService);
        this.clienteService = Objects.requireNonNull(clienteService);
        this.usuarioService = Objects.requireNonNull(usuarioService);
        this.notificador = Objects.requireNonNull(notificador);
        this.usuarioLogado = Objects.requireNonNull(usuarioLogado);
        this.aoSair = Objects.requireNonNull(aoSair);
    }

    public void abrirBuscaProdutos() {
        abrirUnica("busca", () -> {
            BuscaProdutoFrame view = new BuscaProdutoFrame();
            new BuscaProdutoPresenter(view, produtoService, notificador, this);
            return view;
        });
    }

    public void abrirInclusaoProduto() {
        ProdutoFormFrame view = new ProdutoFormFrame();
        new ProdutoFormPresenter(view, produtoService, categoriaService, notificador, null);
        principal.abrirJanela(view);
    }

    public void abrirEdicaoProduto(Long produtoId) {
        abrirUnica("edicao:" + produtoId, () -> {
            ProdutoFormFrame view = new ProdutoFormFrame();
            new ProdutoFormPresenter(view, produtoService, categoriaService, notificador, produtoId);
            return view;
        });
    }

    public void abrirVisualizacaoProduto(Long produtoId) {
        abrirUnica("visualizacao:" + produtoId, () -> {
            ProdutoVisualizacaoFrame view = new ProdutoVisualizacaoFrame();
            new ProdutoVisualizacaoPresenter(view, produtoService, notificador, this, produtoId);
            return view;
        });
    }

    public void abrirHistoricoPrecos(Long produtoId) {
        abrirUnica("historico:" + produtoId, () -> {
            HistoricoPrecoFrame view = new HistoricoPrecoFrame();
            new HistoricoPrecoPresenter(view, produtoService, historicoPrecoService, notificador, produtoId);
            return view;
        });
    }

    public void abrirCategorias() {
        abrirUnica("categorias", () -> {
            CategoriaFrame view = new CategoriaFrame();
            new CategoriaPresenter(view, categoriaService);
            return view;
        });
    }

    public void abrirCalculoMargem() {
        abrirUnica("calculo", () -> {
            CalculoMargemFrame view = new CalculoMargemFrame();
            new CalculoMargemPresenter(view, calculoPrecoService);
            return view;
        });
    }

    public void abrirClientes() {
        abrirUnica("clientes", () -> {
            ClienteFrame view = new ClienteFrame();
            clientePresenter = new ClientePresenter(view, clienteService);
            return view;
        });
    }

    public void abrirInclusaoCliente() {
        abrirClientes();
        clientePresenter.iniciarInclusao();
    }

    public void abrirUsuarios() {
        if (!usuarioLogado.isAdministrador()) {
            return;
        }
        abrirUnica("usuarios", () -> {
            UsuarioFrame view = new UsuarioFrame();
            new UsuarioPresenter(view, usuarioService, clienteService, notificador, usuarioLogado,
                    this::abrirInclusaoCliente);
            return view;
        });
    }

    public void sair() {
        principal.fecharSessao();
        aoSair.run();
    }

    private void abrirUnica(String chave, Supplier<JanelaView> fabrica) {
        JanelaView existente = janelas.get(chave);
        if (existente != null && existente.estaAberta()) {
            existente.trazerParaFrente();
            return;
        }
        JanelaView nova = fabrica.get();
        janelas.put(chave, nova);
        principal.abrirJanela(nova);
    }
}
