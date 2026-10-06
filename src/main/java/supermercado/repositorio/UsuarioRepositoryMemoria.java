package supermercado.repositorio;

import supermercado.model.Usuario;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class UsuarioRepositoryMemoria implements UsuarioRepository {

    private final Map<Long, Usuario> usuarios = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    @Override
    public Usuario salvar(Usuario usuario) {
        Objects.requireNonNull(usuario, "usuario");
        if (usuario.getId() == null) {
            usuario.setId(sequencia.incrementAndGet());
        }
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(id == null ? null : usuarios.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorNomeUsuario(String nomeUsuario) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getNomeUsuario().equalsIgnoreCase(nomeUsuario.trim()))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();
    }

    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public boolean existeUsuarioDoCliente(Long clienteId) {
        return usuarios.values().stream()
                .anyMatch(usuario -> usuario.getCliente() != null
                        && Objects.equals(usuario.getCliente().getId(), clienteId));
    }

    @Override
    public boolean estaVazio() {
        return usuarios.isEmpty();
    }

    @Override
    public void remover(Long id) {
        usuarios.remove(id);
    }
}
