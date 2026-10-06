package supermercado.view;

public interface PrincipalView {

    void exibir();

    void fecharSessao();

    void abrirJanela(JanelaView janela);

    void setUsuarioAtual(String identificacao);

    void setOperacoesDisponiveis(boolean disponiveis);

    void setManutencaoUsuariosDisponivel(boolean disponivel);

    void setAcaoIncluirProdutos(Runnable acao);

    void setAcaoBuscarProdutos(Runnable acao);

    void setAcaoCategorias(Runnable acao);

    void setAcaoCalcularMargem(Runnable acao);

    void setAcaoClientes(Runnable acao);

    void setAcaoUsuarios(Runnable acao);

    void setAcaoSair(Runnable acao);
}
