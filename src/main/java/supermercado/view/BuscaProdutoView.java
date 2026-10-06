package supermercado.view;

import java.util.List;

public interface BuscaProdutoView extends JanelaView {

    void setCriteriosBusca(List<String> criterios);

    int getCriterioSelecionado();

    String getTextoBusca();

    void setProdutos(List<String[]> linhas);

    int getLinhaSelecionada();

    void selecionarLinha(int linha);

    void setVisualizarHabilitado(boolean habilitado);

    void setAcaoBuscar(Runnable acao);

    void setAcaoNovo(Runnable acao);

    void setAcaoVisualizar(Runnable acao);

    void setAcaoSelecaoAlterada(Runnable acao);
}
