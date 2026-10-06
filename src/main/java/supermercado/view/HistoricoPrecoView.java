package supermercado.view;

import java.util.List;

public interface HistoricoPrecoView extends JanelaView {

    void setProduto(String produto);

    void setCategoria(String categoria);

    void setHistorico(List<String[]> linhas);
}
