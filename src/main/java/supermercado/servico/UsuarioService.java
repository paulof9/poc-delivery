package supermercado.servico;

import supermercado.excecao.RegraNegocioException;
import supermercado.excecao.ValidacaoException;
import supermercado.model.Cliente;
import supermercado.model.PerfilUsuario;
import supermercado.model.Usuario;
import supermercado.repositorio.ClienteRepository;
import supermercado.repositorio.UsuarioRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class UsuarioService {

    private static final Pattern FORMATO_EMAIL = Pattern.compile("[^\\s@]+@[^\\s@]+\\.[^\\s@]+");

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final NotificadorAlteracoes notificador;

    public UsuarioService(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository,
            NotificadorAlteracoes notificador) {
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository);
        this.clienteRepository = Objects.requireNonNull(clienteRepository);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.buscarTodos();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.buscarPorId(id);
    }

    public Usuario incluirAdministrador(String nomeCompleto, String email, String nomeUsuario, String senha)
            throws ValidacaoException, RegraNegocioException {
        if (!usuarioRepository.estaVazio()) {
            throw new RegraNegocioException("O Administrador deve ser o primeiro usuário do sistema.");
        }
        validar(null, nomeCompleto, email, nomeUsuario);
        validarSenha(senha);
        Usuario administrador = new Usuario(nomeCompleto.trim(), email.trim(), nomeUsuario.trim(), senha,
                PerfilUsuario.ADMINISTRADOR, null);
        usuarioRepository.salvar(administrador);
        notificador.notificar(TipoAlteracao.USUARIOS);
        return administrador;
    }

    public Usuario incluir(Usuario solicitante, String nomeCompleto, String email, String nomeUsuario,
            String senha, PerfilUsuario perfil, Long clienteId) throws ValidacaoException, RegraNegocioException {
        exigirAdministrador(solicitante);
        if (perfil == PerfilUsuario.ADMINISTRADOR) {
            throw new RegraNegocioException("Não é permitido incluir outro usuário Administrador.");
        }
        validar(null, nomeCompleto, email, nomeUsuario);
        validarSenha(senha);
        Cliente cliente = resolverCliente(perfil, clienteId);
        Usuario usuario = new Usuario(nomeCompleto.trim(), email.trim(), nomeUsuario.trim(), senha,
                perfil, cliente);
        usuarioRepository.salvar(usuario);
        notificador.notificar(TipoAlteracao.USUARIOS);
        return usuario;
    }

    public Usuario alterar(Usuario solicitante, Long id, String nomeCompleto, String email, String nomeUsuario,
            String novaSenha, PerfilUsuario perfil, Long clienteId) throws ValidacaoException, RegraNegocioException {
        exigirAdministrador(solicitante);
        Usuario usuario = buscarExistente(id);
        if (usuario.isAdministrador() != (perfil == PerfilUsuario.ADMINISTRADOR)) {
            throw new RegraNegocioException("O perfil Administrador não pode ser atribuído nem removido.");
        }
        validar(id, nomeCompleto, email, nomeUsuario);
        Cliente cliente = resolverCliente(perfil, clienteId);
        if (novaSenha != null && !novaSenha.isEmpty()) {
            validarSenha(novaSenha);
            usuario.alterarSenha(novaSenha);
        }
        usuario.atualizar(nomeCompleto.trim(), email.trim(), nomeUsuario.trim(), perfil, cliente);
        usuarioRepository.salvar(usuario);
        notificador.notificar(TipoAlteracao.USUARIOS);
        return usuario;
    }

    public void habilitar(Usuario solicitante, Long id) throws RegraNegocioException {
        Usuario usuario = buscarParaManutencao(solicitante, id, "ter o status alterado");
        usuario.habilitar();
        notificador.notificar(TipoAlteracao.USUARIOS);
    }

    public void desabilitar(Usuario solicitante, Long id) throws RegraNegocioException {
        Usuario usuario = buscarParaManutencao(solicitante, id, "ser desabilitado");
        usuario.desabilitar();
        notificador.notificar(TipoAlteracao.USUARIOS);
    }

    public void excluir(Usuario solicitante, Long id) throws RegraNegocioException {
        buscarParaManutencao(solicitante, id, "ser excluído");
        usuarioRepository.remover(id);
        notificador.notificar(TipoAlteracao.USUARIOS);
    }

    private Usuario buscarParaManutencao(Usuario solicitante, Long id, String operacao)
            throws RegraNegocioException {
        exigirAdministrador(solicitante);
        Usuario usuario = buscarExistente(id);
        if (usuario.isAdministrador()) {
            throw new RegraNegocioException("O usuário Administrador não pode " + operacao + ".");
        }
        return usuario;
    }

    private void exigirAdministrador(Usuario solicitante) throws RegraNegocioException {
        if (solicitante == null || !solicitante.isAdministrador()) {
            throw new RegraNegocioException("Somente o Administrador pode realizar a manutenção de usuários.");
        }
    }

    private Usuario buscarExistente(Long id) throws RegraNegocioException {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("O usuário informado não existe no cadastro."));
    }

    private Cliente resolverCliente(PerfilUsuario perfil, Long clienteId)
            throws ValidacaoException, RegraNegocioException {
        if (perfil == null) {
            throw new ValidacaoException("O campo \"Perfil do usuário\" é obrigatório.");
        }
        if (perfil != PerfilUsuario.CLIENTE) {
            return null;
        }
        if (clienteId == null) {
            throw new ValidacaoException("Usuários do perfil Cliente devem estar associados a um cliente.");
        }
        return clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new RegraNegocioException("O cliente selecionado não existe no cadastro."));
    }

    private void validar(Long idAtual, String nomeCompleto, String email, String nomeUsuario)
            throws ValidacaoException {
        exigir(nomeCompleto, "Nome completo");
        exigir(email, "E-mail");
        exigir(nomeUsuario, "Nome de usuário");
        if (!FORMATO_EMAIL.matcher(email.trim()).matches()) {
            throw new ValidacaoException("Informe um e-mail válido.");
        }
        if (nomeUsuario.trim().contains(" ")) {
            throw new ValidacaoException("O nome de usuário não pode conter espaços.");
        }
        Optional<Usuario> mesmoLogin = usuarioRepository.buscarPorNomeUsuario(nomeUsuario);
        if (mesmoLogin.isPresent() && !mesmoLogin.get().getId().equals(idAtual)) {
            throw new ValidacaoException("O nome de usuário \"" + nomeUsuario.trim() + "\" já está em uso.");
        }
        Optional<Usuario> mesmoEmail = usuarioRepository.buscarPorEmail(email);
        if (mesmoEmail.isPresent() && !mesmoEmail.get().getId().equals(idAtual)) {
            throw new ValidacaoException("O e-mail \"" + email.trim() + "\" já está em uso.");
        }
    }

    private void validarSenha(String senha) throws ValidacaoException {
        if (senha == null || senha.isEmpty()) {
            throw new ValidacaoException("O campo \"Senha\" é obrigatório.");
        }
        if (senha.length() < 6) {
            throw new ValidacaoException("A senha deve ter pelo menos 6 caracteres.");
        }
    }

    private void exigir(String valor, String campo) throws ValidacaoException {
        if (valor == null || valor.isBlank()) {
            throw new ValidacaoException("O campo \"" + campo + "\" é obrigatório.");
        }
    }
}
