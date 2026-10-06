package supermercado.servico;

import supermercado.excecao.RegraNegocioException;
import supermercado.excecao.ValidacaoException;
import supermercado.model.Cliente;
import supermercado.repositorio.ClienteRepository;
import supermercado.repositorio.UsuarioRepository;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificadorAlteracoes notificador;

    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository,
            NotificadorAlteracoes notificador) {
        this.clienteRepository = Objects.requireNonNull(clienteRepository);
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.buscarTodos();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.buscarPorId(id);
    }

    public Cliente incluir(String nome, String logradouro, String bairro, String cidade, String uf)
            throws ValidacaoException {
        validar(nome, logradouro, bairro, cidade, uf);
        Cliente cliente = new Cliente(nome.trim(), logradouro.trim(), bairro.trim(), cidade.trim(),
                uf.trim().toUpperCase(Locale.ROOT));
        clienteRepository.salvar(cliente);
        notificador.notificar(TipoAlteracao.CLIENTES);
        return cliente;
    }

    public Cliente alterar(Long id, String nome, String logradouro, String bairro, String cidade, String uf)
            throws ValidacaoException, RegraNegocioException {
        Cliente cliente = buscarExistente(id);
        validar(nome, logradouro, bairro, cidade, uf);
        cliente.atualizar(nome.trim(), logradouro.trim(), bairro.trim(), cidade.trim(),
                uf.trim().toUpperCase(Locale.ROOT));
        clienteRepository.salvar(cliente);
        notificador.notificar(TipoAlteracao.CLIENTES);
        return cliente;
    }

    public void excluir(Long id) throws RegraNegocioException {
        Cliente cliente = buscarExistente(id);
        if (usuarioRepository.existeUsuarioDoCliente(id)) {
            throw new RegraNegocioException("O cliente \"" + cliente.getNome()
                    + "\" não pode ser excluído porque existe usuário associado a ele.");
        }
        clienteRepository.remover(id);
        notificador.notificar(TipoAlteracao.CLIENTES);
    }

    public void registrarCompra(Long id, double valor) throws RegraNegocioException {
        buscarExistente(id).registrarCompra(valor);
        notificador.notificar(TipoAlteracao.CLIENTES);
    }

    private Cliente buscarExistente(Long id) throws RegraNegocioException {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("O cliente informado não existe no cadastro."));
    }

    private void validar(String nome, String logradouro, String bairro, String cidade, String uf)
            throws ValidacaoException {
        exigir(nome, "Nome");
        exigir(logradouro, "Logradouro");
        exigir(bairro, "Bairro");
        exigir(cidade, "Cidade");
        exigir(uf, "UF");
        if (!uf.trim().matches("[A-Za-z]{2}")) {
            throw new ValidacaoException("O campo \"UF\" deve conter a sigla do estado com duas letras.");
        }
    }

    private void exigir(String valor, String campo) throws ValidacaoException {
        if (valor == null || valor.isBlank()) {
            throw new ValidacaoException("O campo \"" + campo + "\" é obrigatório.");
        }
    }
}
