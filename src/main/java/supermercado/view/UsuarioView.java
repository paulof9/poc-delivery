package supermercado.view;

import java.util.List;

public interface UsuarioView extends JanelaView {

    String getNomeCompleto();

    void setNomeCompleto(String nome);

    String getEmail();

    void setEmail(String email);

    String getNomeUsuario();

    void setNomeUsuario(String nomeUsuario);

    String getSenha();

    String getConfirmacaoSenha();

    void setSenhas(String senha);

    void setPerfis(List<String> perfis);

    String getPerfil();

    void setPerfil(String perfil);

    void setStatus(String status);

    void setClientes(List<String> nomes);

    int getClienteSelecionado();

    void setClienteSelecionado(int indice);

    void setModo(String modo);

    void setCamposEditaveis(boolean editaveis);

    void setPerfilEditavel(boolean editavel);

    void setClienteEditavel(boolean editavel);

    void setBotoesHabilitados(boolean novo, boolean editar, boolean excluir, boolean habilitar,
            boolean desabilitar, boolean salvar, boolean cancelar, boolean fechar);

    void setTabelaHabilitada(boolean habilitada);

    void setFechamentoPermitido(boolean permitido);

    void setUsuarios(List<String[]> linhas);

    int getLinhaSelecionada();

    void selecionarLinha(int linha);

    void focarNome();

    void setAcaoNovo(Runnable acao);

    void setAcaoEditar(Runnable acao);

    void setAcaoExcluir(Runnable acao);

    void setAcaoHabilitar(Runnable acao);

    void setAcaoDesabilitar(Runnable acao);

    void setAcaoSalvar(Runnable acao);

    void setAcaoCancelar(Runnable acao);

    void setAcaoSelecaoAlterada(Runnable acao);

    void setAcaoPerfilAlterado(Runnable acao);

    void setAcaoIncluirCliente(Runnable acao);
}
