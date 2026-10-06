package supermercado.view;

import java.util.List;

public interface CalculoMargemView extends JanelaView {

    String getDataCalculo();

    void setDataCalculo(String data);

    void setResultados(List<String[]> linhas);

    void setAcaoCalcular(Runnable acao);
}
