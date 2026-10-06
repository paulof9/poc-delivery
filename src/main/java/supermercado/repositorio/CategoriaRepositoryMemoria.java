package supermercado.repositorio;

import supermercado.model.Categoria;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class CategoriaRepositoryMemoria implements CategoriaRepository {

    private final Map<Long, Categoria> categorias = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    @Override
    public Categoria salvar(Categoria categoria) {
        Objects.requireNonNull(categoria, "categoria");
        if (categoria.getId() == null) {
            categoria.setId(sequencia.incrementAndGet());
        }
        categorias.put(categoria.getId(), categoria);
        return categoria;
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return Optional.ofNullable(id == null ? null : categorias.get(id));
    }

    @Override
    public Optional<Categoria> buscarPorNome(String nome) {
        return categorias.values().stream()
                .filter(categoria -> categoria.possuiNome(nome))
                .findFirst();
    }

    @Override
    public List<Categoria> buscarTodas() {
        return new ArrayList<>(categorias.values());
    }

    @Override
    public void remover(Long id) {
        categorias.remove(id);
    }
}
