package supermercado.fakes;

import supermercado.view.UsuarioView;
import java.util.ArrayList;
import java.util.List;

public class UsuarioViewFalsa extends JanelaFalsa implements UsuarioView {

    public String nomeCompleto = "";
    public String email = "";
    public String nomeUsuario = "";
    public String senha = "";
    public String confirmacaoSenha = "";
    public List<String> perfis = new ArrayList<>();
    public String perfil;
    public String status = "";
    public List<String> clientes = new ArrayList<>();
    public int clienteSelecionado = -1;
    public String modo = "";
    public boolean camposEditaveis;
    public boolean perfilEditavel;
    public boolean clienteEditavel;
    public boolean novoHabilitado;
    public boolean editarHabilitado;
    public boolean excluirHabilitado;
    public boolean habilitarHabilitado;
    public boolean desabilitarHabilitado;
    public boolean salvarHabilitado;
    public boolean cancelarHabilitado;
    public boolean fecharHabilitado;
    public boolean tabelaHabilitada;
    public List<String[]> linhas = new ArrayList<>();
    public int linhaSelecionada = -1;
    private Runnable acaoNovo;
    private Runnable acaoEditar;
    private Runnable acaoExcluir;
    private Runnable acaoHabilitar;
    private Runnable acaoDesabilitar;
    private Runnable acaoSalvar;
    private Runnable acaoCancelar;
    private Runnable acaoSelecaoAlterada;
    private Runnable acaoPerfilAlterado;
    private Runnable acaoIncluirCliente;

    public void preencher(String nomeCompleto, String email, String nomeUsuario, String senha,
            String confirmacaoSenha) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.confirmacaoSenha = confirmacaoSenha;
    }

    public void escolherPerfil(String perfil) {
        if (!perfis.contains(perfil)) {
            throw new IllegalArgumentException("Perfil indisponível na lista: " + perfil);
        }
        setPerfil(perfil);
    }

    public void escolherCliente(String nomeCliente) {
        int indice = clientes.indexOf(nomeCliente);
        if (indice < 0) {
            throw new IllegalArgumentException("Cliente indisponível na lista: " + nomeCliente);
        }
        clienteSelecionado = indice;
    }

    public String clienteEscolhido() {
        return clienteSelecionado < 0 ? null : clientes.get(clienteSelecionado);
    }

    public void selecionarNaTabela(String nomeUsuario) {
        for (int i = 0; i < linhas.size(); i++) {
            if (linhas.get(i)[2].equals(nomeUsuario)) {
                selecionarLinha(i);
                return;
            }
        }
        throw new IllegalArgumentException("Usuário não está na tabela: " + nomeUsuario);
    }

    public boolean tabelaContem(String nomeUsuario) {
        return linhas.stream().anyMatch(linha -> linha[2].equals(nomeUsuario));
    }

    public String[] linhaDaTabela(String nomeUsuario) {
        return linhas.stream().filter(linha -> linha[2].equals(nomeUsuario)).findFirst().orElseThrow();
    }

    public void clicarNovo() {
        acaoNovo.run();
    }

    public void clicarEditar() {
        acaoEditar.run();
    }

    public void clicarExcluir() {
        acaoExcluir.run();
    }

    public void clicarHabilitar() {
        acaoHabilitar.run();
    }

    public void clicarDesabilitar() {
        acaoDesabilitar.run();
    }

    public void clicarSalvar() {
        acaoSalvar.run();
    }

    public void clicarCancelar() {
        acaoCancelar.run();
    }

    public void clicarIncluirCliente() {
        acaoIncluirCliente.run();
    }

    @Override
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    @Override
    public void setNomeCompleto(String nome) {
        nomeCompleto = nome;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    @Override
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    @Override
    public String getSenha() {
        return senha;
    }

    @Override
    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    @Override
    public void setSenhas(String senha) {
        this.senha = senha;
        confirmacaoSenha = senha;
    }

    @Override
    public void setPerfis(List<String> perfis) {
        this.perfis = new ArrayList<>(perfis);
        setPerfil(perfis.isEmpty() ? null : perfis.get(0));
    }

    @Override
    public String getPerfil() {
        return perfil;
    }

    @Override
    public void setPerfil(String perfil) {
        if (perfil == null || perfis.contains(perfil)) {
            this.perfil = perfil;
            if (acaoPerfilAlterado != null) {
                acaoPerfilAlterado.run();
            }
        }
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void setClientes(List<String> nomes) {
        clientes = new ArrayList<>(nomes);
        clienteSelecionado = -1;
    }

    @Override
    public int getClienteSelecionado() {
        return clienteSelecionado;
    }

    @Override
    public void setClienteSelecionado(int indice) {
        clienteSelecionado = indice >= 0 && indice < clientes.size() ? indice : -1;
    }

    @Override
    public void setModo(String modo) {
        this.modo = modo;
    }

    @Override
    public void setCamposEditaveis(boolean editaveis) {
        camposEditaveis = editaveis;
    }

    @Override
    public void setPerfilEditavel(boolean editavel) {
        perfilEditavel = editavel;
    }

    @Override
    public void setClienteEditavel(boolean editavel) {
        clienteEditavel = editavel;
    }

    @Override
    public void setBotoesHabilitados(boolean novo, boolean editar, boolean excluir, boolean habilitar,
            boolean desabilitar, boolean salvar, boolean cancelar, boolean fechar) {
        novoHabilitado = novo;
        editarHabilitado = editar;
        excluirHabilitado = excluir;
        habilitarHabilitado = habilitar;
        desabilitarHabilitado = desabilitar;
        salvarHabilitado = salvar;
        cancelarHabilitado = cancelar;
        fecharHabilitado = fechar;
    }

    @Override
    public void setTabelaHabilitada(boolean habilitada) {
        tabelaHabilitada = habilitada;
    }

    @Override
    public void setFechamentoPermitido(boolean permitido) {
    }

    @Override
    public void setUsuarios(List<String[]> linhas) {
        this.linhas = new ArrayList<>(linhas);
        linhaSelecionada = -1;
    }

    @Override
    public int getLinhaSelecionada() {
        return linhaSelecionada;
    }

    @Override
    public void selecionarLinha(int linha) {
        linhaSelecionada = linha;
        if (acaoSelecaoAlterada != null) {
            acaoSelecaoAlterada.run();
        }
    }

    @Override
    public void focarNome() {
    }

    @Override
    public void setAcaoNovo(Runnable acao) {
        acaoNovo = acao;
    }

    @Override
    public void setAcaoEditar(Runnable acao) {
        acaoEditar = acao;
    }

    @Override
    public void setAcaoExcluir(Runnable acao) {
        acaoExcluir = acao;
    }

    @Override
    public void setAcaoHabilitar(Runnable acao) {
        acaoHabilitar = acao;
    }

    @Override
    public void setAcaoDesabilitar(Runnable acao) {
        acaoDesabilitar = acao;
    }

    @Override
    public void setAcaoSalvar(Runnable acao) {
        acaoSalvar = acao;
    }

    @Override
    public void setAcaoCancelar(Runnable acao) {
        acaoCancelar = acao;
    }

    @Override
    public void setAcaoSelecaoAlterada(Runnable acao) {
        acaoSelecaoAlterada = acao;
    }

    @Override
    public void setAcaoPerfilAlterado(Runnable acao) {
        acaoPerfilAlterado = acao;
    }

    @Override
    public void setAcaoIncluirCliente(Runnable acao) {
        acaoIncluirCliente = acao;
    }
}
