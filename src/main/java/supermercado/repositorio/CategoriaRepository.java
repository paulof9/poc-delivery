package supermercado.repositorio;

import supermercado.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {

    Categoria salvar(Categoria categoria);

    Optional<Categoria> buscarPorId(Long id);

    Optional<Categoria> buscarPorNome(String nome);

    List<Categoria> buscarTodas();

    void remover(Long id);
}
