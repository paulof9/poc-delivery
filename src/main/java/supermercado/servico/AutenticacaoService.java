package supermercado.servico;

import supermercado.excecao.RegraNegocioException;
import supermercado.excecao.ValidacaoException;
import supermercado.model.Usuario;
import supermercado.repositorio.UsuarioRepository;
import java.util.Objects;
import java.util.Optional;

public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository);
    }

    public Usuario autenticar(String identificacao, String senha)
            throws ValidacaoException, RegraNegocioException {
        if (identificacao == null || identificacao.isBlank() || senha == null || senha.isEmpty()) {
            throw new ValidacaoException("Preencha os campos \"Usuário ou e-mail\" e \"Senha\".");
        }
        Optional<Usuario> encontrado = usuarioRepository.buscarPorNomeUsuario(identificacao)
                .or(() -> usuarioRepository.buscarPorEmail(identificacao));
        if (encontrado.isEmpty() || !encontrado.get().possuiSenha(senha)) {
            throw new RegraNegocioException("Credenciais inválidas.");
        }
        Usuario usuario = encontrado.get();
        if (!usuario.isHabilitado()) {
            throw new RegraNegocioException("Acesso negado: o usuário está desabilitado.");
        }
        return usuario;
    }
}
