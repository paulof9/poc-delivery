package supermercado.repositorio;

import supermercado.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    List<Cliente> buscarTodos();

    void remover(Long id);
}
