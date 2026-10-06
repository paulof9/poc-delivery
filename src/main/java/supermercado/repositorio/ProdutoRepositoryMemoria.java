package supermercado.repositorio;

import supermercado.model.Produto;
import supermercado.util.Textos;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ProdutoRepositoryMemoria implements ProdutoRepository {

    private final Map<Long, Produto> produtos = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    @Override
    public Produto salvar(Produto produto) {
        Objects.requireNonNull(produto, "produto");
        if (produto.getId() == null) {
            produto.setId(sequencia.incrementAndGet());
        }
        produtos.put(produto.getId(), produto);
        return produto;
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return Optional.ofNullable(id == null ? null : produtos.get(id));
    }

    @Override
    public List<Produto> buscarTodos() {
        return new ArrayList<>(produtos.values());
    }

    @Override
    public List<Produto> buscarPorNome(String trecho) {
        return produtos.values().stream()
                .filter(produto -> Textos.contem(produto.getNome(), trecho))
                .toList();
    }

    @Override
    public List<Produto> buscarPorNomeCategoria(String trecho) {
        return produtos.values().stream()
                .filter(produto -> Textos.contem(produto.getCategoria().getNome(), trecho))
                .toList();
    }

    @Override
    public boolean existeProdutoNaCategoria(Long categoriaId) {
        return produtos.values().stream()
                .anyMatch(produto -> produto.pertenceACategoria(categoriaId));
    }
}
