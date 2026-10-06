package supermercado.servico;

import supermercado.excecao.RegraNegocioException;
import supermercado.excecao.ValidacaoException;
import supermercado.model.Categoria;
import supermercado.repositorio.CategoriaRepository;
import supermercado.repositorio.ProdutoRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final NotificadorAlteracoes notificador;

    public CategoriaService(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository,
            NotificadorAlteracoes notificador) {
        this.categoriaRepository = Objects.requireNonNull(categoriaRepository);
        this.produtoRepository = Objects.requireNonNull(produtoRepository);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.buscarTodas();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.buscarPorId(id);
    }

    public Categoria incluir(String nome, Double percentualLucro) throws ValidacaoException {
        validar(null, nome, percentualLucro);
        Categoria categoria = categoriaRepository.salvar(new Categoria(nome.trim(), percentualLucro));
        notificador.notificar(TipoAlteracao.CATEGORIAS);
        return categoria;
    }

    public Categoria alterar(Long id, String nome, Double percentualLucro)
            throws ValidacaoException, RegraNegocioException {
        Categoria categoria = buscarExistente(id);
        validar(id, nome, percentualLucro);
        categoria.atualizar(nome.trim(), percentualLucro);
        categoriaRepository.salvar(categoria);
        notificador.notificar(TipoAlteracao.CATEGORIAS);
        return categoria;
    }

    public void excluir(Long id) throws RegraNegocioException {
        Categoria categoria = buscarExistente(id);
        if (produtoRepository.existeProdutoNaCategoria(id)) {
            throw new RegraNegocioException("A categoria \"" + categoria.getNome()
                    + "\" não pode ser excluída porque existem produtos associados a ela.");
        }
        categoriaRepository.remover(id);
        notificador.notificar(TipoAlteracao.CATEGORIAS);
    }

    private Categoria buscarExistente(Long id) throws RegraNegocioException {
        return categoriaRepository.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("A categoria informada não existe no cadastro."));
    }

    private void validar(Long idAtual, String nome, Double percentualLucro) throws ValidacaoException {
        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("O nome da categoria é obrigatório.");
        }
        Optional<Categoria> mesmoNome = categoriaRepository.buscarPorNome(nome);
        if (mesmoNome.isPresent() && !mesmoNome.get().getId().equals(idAtual)) {
            throw new ValidacaoException("Já existe uma categoria com o nome \""
                    + mesmoNome.get().getNome() + "\".");
        }
        if (percentualLucro == null) {
            throw new ValidacaoException("O percentual de lucro da categoria é obrigatório.");
        }
        if (percentualLucro.isNaN() || percentualLucro.isInfinite() || percentualLucro < 0) {
            throw new ValidacaoException("O percentual de lucro deve ser maior ou igual a zero.");
        }
    }
}
