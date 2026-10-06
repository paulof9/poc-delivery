package supermercado.view;

import java.util.List;

public interface ProdutoFormView extends JanelaView {

    String getNome();

    void setNome(String nome);

    String getPrecoCusto();

    void setPrecoCusto(String precoCusto);

    void setCategorias(List<String> nomes);

    int getCategoriaSelecionada();

    void setCategoriaSelecionada(int indice);

    void setMargemLucro(String margem);

    void setPrecoVenda(String precoVenda);

    void setAcaoSalvar(Runnable acao);

    void setAcaoCancelar(Runnable acao);
}
