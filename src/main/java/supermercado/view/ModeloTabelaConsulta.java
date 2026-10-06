package supermercado.view;

import javax.swing.table.DefaultTableModel;

final class ModeloTabelaConsulta extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    ModeloTabelaConsulta(String... colunas) {
        super(colunas, 0);
    }

    @Override
    public boolean isCellEditable(int linha, int coluna) {
        return false;
    }
}
