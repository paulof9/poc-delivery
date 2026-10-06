package supermercado.fakes;

import supermercado.view.ClienteView;
import java.util.ArrayList;
import java.util.List;

public class ClienteViewFalsa extends JanelaFalsa implements ClienteView {

    public String nome = "";
    public String logradouro = "";
    public String bairro = "";
    public String cidade = "";
    public String uf = "";
    public String tipoCliente = "";
    public String totalCompras = "";
    public String modo = "";
    public boolean camposEditaveis;
    public boolean novoHabilitado;
    public boolean editarHabilitado;
    public boolean excluirHabilitado;
    public boolean salvarHabilitado;
    public boolean cancelarHabilitado;
    public boolean fecharHabilitado;
    public boolean tabelaHabilitada;
    public List<String[]> linhas = new ArrayList<>();
    public int linhaSelecionada = -1;
    private Runnable acaoNovo;
    private Runnable acaoEditar;
    private Runnable acaoExcluir;
    private Runnable acaoSalvar;
    private Runnable acaoCancelar;
    private Runnable acaoSelecaoAlterada;

    public void preencher(String nome, String logradouro, String bairro, String cidade, String uf) {
        this.nome = nome;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }

    public void selecionarNaTabela(String nomeCliente) {
        for (int i = 0; i < linhas.size(); i++) {
            if (linhas.get(i)[0].equals(nomeCliente)) {
                selecionarLinha(i);
                return;
            }
        }
        throw new IllegalArgumentException("Cliente não está na tabela: " + nomeCliente);
    }

    public boolean tabelaContem(String nomeCliente) {
        return linhas.stream().anyMatch(linha -> linha[0].equals(nomeCliente));
    }

    public String[] linhaDaTabela(String nomeCliente) {
        return linhas.stream().filter(linha -> linha[0].equals(nomeCliente)).findFirst().orElseThrow();
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

    public void clicarSalvar() {
        acaoSalvar.run();
    }

    public void clicarCancelar() {
        acaoCancelar.run();
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getLogradouro() {
        return logradouro;
    }

    @Override
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    @Override
    public String getBairro() {
        return bairro;
    }

    @Override
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    @Override
    public String getCidade() {
        return cidade;
    }

    @Override
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public String getUf() {
        return uf;
    }

    @Override
    public void setUf(String uf) {
        this.uf = uf;
    }

    @Override
    public void setTipoCliente(String tipo) {
        tipoCliente = tipo;
    }

    @Override
    public void setTotalCompras(String total) {
        totalCompras = total;
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
    public void setBotoesHabilitados(boolean novo, boolean editar, boolean excluir,
            boolean salvar, boolean cancelar, boolean fechar) {
        novoHabilitado = novo;
        editarHabilitado = editar;
        excluirHabilitado = excluir;
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
    public void setClientes(List<String[]> linhas) {
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
}
