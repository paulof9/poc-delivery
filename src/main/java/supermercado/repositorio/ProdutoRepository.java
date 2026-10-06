package supermercado.repositorio;

import supermercado.model.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {

    Produto salvar(Produto produto);

    Optional<Produto> buscarPorId(Long id);

    List<Produto> buscarTodos();

    List<Produto> buscarPorNome(String trecho);

    List<Produto> buscarPorNomeCategoria(String trecho);

    boolean existeProdutoNaCategoria(Long categoriaId);
}
