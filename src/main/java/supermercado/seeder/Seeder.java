package supermercado.seeder;

import supermercado.excecao.NegocioException;
import supermercado.model.Categoria;
import supermercado.model.Cliente;
import supermercado.model.PerfilUsuario;
import supermercado.model.Usuario;
import supermercado.servico.CalculoPrecoService;
import supermercado.servico.CategoriaService;
import supermercado.servico.ClienteService;
import supermercado.servico.ProdutoService;
import supermercado.servico.UsuarioService;
import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Seeder {

    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;
    private final CalculoPrecoService calculoPrecoService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final Clock relogio;

    public Seeder(CategoriaService categoriaService, ProdutoService produtoService,
            CalculoPrecoService calculoPrecoService, ClienteService clienteService, UsuarioService usuarioService) {
        this(categoriaService, produtoService, calculoPrecoService, clienteService, usuarioService,
                Clock.systemDefaultZone());
    }

    public Seeder(CategoriaService categoriaService, ProdutoService produtoService,
            CalculoPrecoService calculoPrecoService, ClienteService clienteService, UsuarioService usuarioService,
            Clock relogio) {
        this.categoriaService = Objects.requireNonNull(categoriaService);
        this.produtoService = Objects.requireNonNull(produtoService);
        this.calculoPrecoService = Objects.requireNonNull(calculoPrecoService);
        this.clienteService = Objects.requireNonNull(clienteService);
        this.usuarioService = Objects.requireNonNull(usuarioService);
        this.relogio = Objects.requireNonNull(relogio);
    }

    public void executar() throws NegocioException {
        carregarProdutos();
        carregarUsuarios(carregarClientes());
    }

    private void carregarProdutos() throws NegocioException {
        Map<String, Categoria> categorias = new HashMap<>();
        incluirCategoria(categorias, "Educação", 25.00);
        incluirCategoria(categorias, "Papelaria", 30.00);
        incluirCategoria(categorias, "Alimentação", 22.00);
        incluirCategoria(categorias, "Lazer", 35.00);
        incluirCategoria(categorias, "Entretenimento", 40.00);
        incluirCategoria(categorias, "Higiene", 28.00);
        incluirCategoria(categorias, "Limpeza", 25.00);

        incluirProduto(categorias, "Livro didático", "Educação", 45.00);
        incluirProduto(categorias, "Livro paradidático", "Educação", 30.00);
        incluirProduto(categorias, "Mochila escolar", "Educação", 70.00);
        incluirProduto(categorias, "Caderno universitário", "Papelaria", 16.00);
        incluirProduto(categorias, "Lápis grafite HB", "Papelaria", 1.20);
        incluirProduto(categorias, "Caneta esferográfica azul", "Papelaria", 2.20);
        incluirProduto(categorias, "Borracha branca", "Papelaria", 1.00);
        incluirProduto(categorias, "Apontador com depósito", "Papelaria", 3.50);
        incluirProduto(categorias, "Jogo de tabuleiro", "Lazer", 55.00);
        incluirProduto(categorias, "Bola recreativa", "Lazer", 40.00);
        incluirProduto(categorias, "Quebra-cabeça 500 peças", "Lazer", 35.00);
        incluirProduto(categorias, "Fone de ouvido", "Entretenimento", 48.00);
        incluirProduto(categorias, "Caixa de som portátil", "Entretenimento", 80.00);
        incluirProduto(categorias, "Revista de passatempos", "Entretenimento", 12.00);
        incluirProduto(categorias, "Biscoito integral", "Alimentação", 5.50);
        incluirProduto(categorias, "Suco de uva 1 L", "Alimentação", 9.00);
        incluirProduto(categorias, "Barra de cereal", "Alimentação", 3.20);
        incluirProduto(categorias, "Sabonete", "Higiene", 2.80);
        incluirProduto(categorias, "Creme dental", "Higiene", 5.50);
        incluirProduto(categorias, "Detergente líquido", "Limpeza", 2.60);
        incluirProduto(categorias, "Esponja multiuso", "Limpeza", 1.70);

        LocalDate dataInicial = LocalDate.now(relogio).minusDays(CalculoPrecoService.INTERVALO_MINIMO_DIAS);
        calculoPrecoService.calcular(dataInicial);
    }

    private Map<String, Cliente> carregarClientes() throws NegocioException {
        Map<String, Cliente> clientes = new HashMap<>();
        incluirCliente(clientes, "Ana Souza", "Rua das Flores, 123", "Centro", "Alegre", 245.80);
        incluirCliente(clientes, "Bruno Lima", "Av. Brasil, 45", "Vila do Sul", "Alegre", 120.00);
        incluirCliente(clientes, "Carlos Mendes", "Rua Central, 8", "Centro", "Guaçuí", 450.30);
        incluirCliente(clientes, "Daniela Rocha", "Rua Nova, 77", "Nova Alegre", "Alegre", 89.90);
        incluirCliente(clientes, "Eduardo Silva", "Rua São José, 310", "São Miguel", "Guaçuí", 310.20);
        incluirCliente(clientes, "Fernanda Costa", "Av. Rio Branco, 52", "Centro", "Alegre", 560.75);
        incluirCliente(clientes, "Gabriel Oliveira", "Rua do Café, 19", "Vila do Café", "Alegre", 75.40);
        incluirCliente(clientes, "Juliana Santos", "Rua Sete de Setembro, 90", "Centro", "Guaçuí", 220.00);
        incluirCliente(clientes, "Marcos Ribeiro", "Rua da Ponte, 14", "Cachoeirinha", "Alegre", 180.50);
        incluirCliente(clientes, "Patrícia Almeida", "Rua das Acácias, 7", "Bela Vista", "Guaçuí", 99.00);
        return clientes;
    }

    private void carregarUsuarios(Map<String, Cliente> clientes) throws NegocioException {
        Usuario admin = usuarioService.incluirAdministrador("Administrador do Sistema", "admin@pocdelivery.com",
                "admin", "admin123");
        usuarioService.incluir(admin, "Fernanda Alves", "fernanda@pocdelivery.com", "fernanda", "fernanda123",
                PerfilUsuario.ATENDENTE, null);
        usuarioService.incluir(admin, "Ricardo Nascimento", "ricardo@pocdelivery.com", "ricardo", "ricardo123",
                PerfilUsuario.ATENDENTE, null);
        usuarioService.incluir(admin, "Ana Souza", "ana@cliente.com", "anasouza", "ana12345",
                PerfilUsuario.CLIENTE, clientes.get("Ana Souza").getId());
        usuarioService.incluir(admin, "Bruno Lima", "bruno@cliente.com", "brunolima", "bruno123",
                PerfilUsuario.CLIENTE, clientes.get("Bruno Lima").getId());
        Usuario diego = usuarioService.incluir(admin, "Diego Rocha", "diego@pocdelivery.com", "diego",
                "diego123", PerfilUsuario.ATENDENTE, null);
        usuarioService.desabilitar(admin, diego.getId());
    }

    private void incluirCategoria(Map<String, Categoria> categorias, String nome, double percentual)
            throws NegocioException {
        categorias.put(nome, categoriaService.incluir(nome, percentual));
    }

    private void incluirProduto(Map<String, Categoria> categorias, String nome, String categoria, double precoCusto)
            throws NegocioException {
        produtoService.incluir(nome, precoCusto, categorias.get(categoria).getId());
    }

    private void incluirCliente(Map<String, Cliente> clientes, String nome, String logradouro, String bairro,
            String cidade, double totalCompras) throws NegocioException {
        Cliente cliente = clienteService.incluir(nome, logradouro, bairro, cidade, "ES");
        clienteService.registrarCompra(cliente.getId(), totalCompras);
        clientes.put(nome, cliente);
    }
}
