package supermercado.repositorio;

import supermercado.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorNomeUsuario(String nomeUsuario);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> buscarTodos();

    boolean existeUsuarioDoCliente(Long clienteId);

    boolean estaVazio();

    void remover(Long id);
}
