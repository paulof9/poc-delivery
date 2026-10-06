package supermercado.servico;

import supermercado.excecao.RegraNegocioException;
import supermercado.excecao.ValidacaoException;
import supermercado.model.Categoria;
import supermercado.model.Produto;
import supermercado.repositorio.CategoriaRepository;
import supermercado.repositorio.ProdutoRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final NotificadorAlteracoes notificador;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository,
            NotificadorAlteracoes notificador) {
        this.produtoRepository = Objects.requireNonNull(produtoRepository);
        this.categoriaRepository = Objects.requireNonNull(categoriaRepository);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.buscarTodos();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.buscarPorId(id);
    }

    public List<Produto> buscar(CriterioBuscaProduto criterio, String texto) {
        if (texto == null || texto.isBlank()) {
            return produtoRepository.buscarTodos();
        }
        return switch (criterio == null ? CriterioBuscaProduto.NOME : criterio) {
            case NOME -> produtoRepository.buscarPorNome(texto);
            case CATEGORIA -> produtoRepository.buscarPorNomeCategoria(texto);
        };
    }

    public Produto incluir(String nome, Double precoCusto, Long categoriaId) throws ValidacaoException {
        Categoria categoria = validar(nome, precoCusto, categoriaId);
        Produto produto = produtoRepository.salvar(new Produto(nome.trim(), precoCusto, categoria));
        notificador.notificar(TipoAlteracao.PRODUTOS);
        return produto;
    }

    public Produto alterar(Long id, String nome, Double precoCusto, Long categoriaId)
            throws ValidacaoException, RegraNegocioException {
        Produto produto = produtoRepository.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("O produto informado não existe no cadastro."));
        Categoria categoria = validar(nome, precoCusto, categoriaId);
        produto.atualizarDados(nome.trim(), precoCusto, categoria);
        produtoRepository.salvar(produto);
        notificador.notificar(TipoAlteracao.PRODUTOS);
        return produto;
    }

    private Categoria validar(String nome, Double precoCusto, Long categoriaId) throws ValidacaoException {
        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("O nome do produto é obrigatório.");
        }
        if (precoCusto == null) {
            throw new ValidacaoException("O preço de custo é obrigatório.");
        }
        if (precoCusto.isNaN() || precoCusto.isInfinite() || precoCusto <= 0) {
            throw new ValidacaoException("O preço de custo deve ser maior que zero.");
        }
        if (categoriaId == null) {
            throw new ValidacaoException("A categoria do produto é obrigatória.");
        }
        return categoriaRepository.buscarPorId(categoriaId)
                .orElseThrow(() -> new ValidacaoException("A categoria selecionada não existe no cadastro."));
    }
}
