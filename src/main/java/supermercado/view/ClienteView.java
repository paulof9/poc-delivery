package supermercado.view;

import java.util.List;

public interface ClienteView extends JanelaView {

    String getNome();

    void setNome(String nome);

    String getLogradouro();

    void setLogradouro(String logradouro);

    String getBairro();

    void setBairro(String bairro);

    String getCidade();

    void setCidade(String cidade);

    String getUf();

    void setUf(String uf);

    void setTipoCliente(String tipo);

    void setTotalCompras(String total);

    void setModo(String modo);

    void setCamposEditaveis(boolean editaveis);

    void setBotoesHabilitados(boolean novo, boolean editar, boolean excluir,
            boolean salvar, boolean cancelar, boolean fechar);

    void setTabelaHabilitada(boolean habilitada);

    void setFechamentoPermitido(boolean permitido);

    void setClientes(List<String[]> linhas);

    int getLinhaSelecionada();

    void selecionarLinha(int linha);

    void focarNome();

    void setAcaoNovo(Runnable acao);

    void setAcaoEditar(Runnable acao);

    void setAcaoExcluir(Runnable acao);

    void setAcaoSalvar(Runnable acao);

    void setAcaoCancelar(Runnable acao);

    void setAcaoSelecaoAlterada(Runnable acao);
}
