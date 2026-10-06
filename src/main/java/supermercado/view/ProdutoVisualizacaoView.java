package supermercado.view;

public interface ProdutoVisualizacaoView extends JanelaView {

    void setNome(String nome);

    void setPrecoCusto(String precoCusto);

    void setCategoria(String categoria);

    void setMargemLucro(String margem);

    void setPrecoVenda(String precoVenda);

    void setAcaoHistorico(Runnable acao);

    void setAcaoEditar(Runnable acao);
}
