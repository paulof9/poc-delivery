package supermercado.repositorio;

import supermercado.model.Cliente;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteRepositoryMemoria implements ClienteRepository {

    private final Map<Long, Cliente> clientes = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    @Override
    public Cliente salvar(Cliente cliente) {
        Objects.requireNonNull(cliente, "cliente");
        if (cliente.getId() == null) {
            cliente.setId(sequencia.incrementAndGet());
        }
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return Optional.ofNullable(id == null ? null : clientes.get(id));
    }

    @Override
    public List<Cliente> buscarTodos() {
        return new ArrayList<>(clientes.values());
    }

    @Override
    public void remover(Long id) {
        clientes.remove(id);
    }
}
