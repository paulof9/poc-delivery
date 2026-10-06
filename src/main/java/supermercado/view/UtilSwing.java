package supermercado.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

final class UtilSwing {

    private static final Color FUNDO_SOMENTE_LEITURA = new Color(226, 226, 226);

    private UtilSwing() {
    }

    static void executar(Runnable acao) {
        if (acao != null) {
            acao.run();
        }
    }

    static DefaultTableModel configurarTabela(JTable tabela, String... colunas) {
        ModeloTabelaConsulta modelo = new ModeloTabelaConsulta(colunas);
        tabela.setModel(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setFillsViewportHeight(true);
        tabela.setRowHeight(Math.max(tabela.getRowHeight(), 22));
        return modelo;
    }

    static void definirLarguras(JTable tabela, int... larguras) {
        for (int coluna = 0; coluna < larguras.length; coluna++) {
            tabela.getColumnModel().getColumn(coluna).setPreferredWidth(larguras[coluna]);
        }
    }

    static void definirSomenteLeitura(boolean somenteLeitura, JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setEditable(!somenteLeitura);
            campo.setBackground(somenteLeitura ? FUNDO_SOMENTE_LEITURA : UIManager.getColor("TextField.background"));
            if (!(campo.getCaret() instanceof CursorSomenteEdicao)) {
                CursorSomenteEdicao cursor = new CursorSomenteEdicao();
                cursor.setBlinkRate(campo.getCaret().getBlinkRate());
                campo.setCaret(cursor);
            }
        }
    }

    static void alinharColunas(JTable tabela, int alinhamento, int... colunas) {
        DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer();
        renderizador.setHorizontalAlignment(alinhamento);
        for (int coluna : colunas) {
            tabela.getColumnModel().getColumn(coluna).setCellRenderer(renderizador);
        }
    }

    static void substituirLinhas(DefaultTableModel modelo, List<String[]> linhas) {
        modelo.setRowCount(0);
        for (String[] linha : linhas) {
            modelo.addRow(linha);
        }
    }

    static int linhaSelecionada(JTable tabela) {
        int linha = tabela.getSelectedRow();
        return linha < 0 ? -1 : tabela.convertRowIndexToModel(linha);
    }

    static void selecionarLinha(JTable tabela, int linhaModelo) {
        if (linhaModelo < 0 || linhaModelo >= tabela.getModel().getRowCount()) {
            tabela.clearSelection();
            return;
        }
        int linha = tabela.convertRowIndexToView(linhaModelo);
        tabela.setRowSelectionInterval(linha, linha);
        tabela.scrollRectToVisible(tabela.getCellRect(linha, 0, true));
    }

    static void aoSelecionarLinha(JTable tabela, Runnable acao) {
        tabela.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                acao.run();
            }
        });
    }

    static void aoDuploClique(JTable tabela, Runnable acao) {
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                if (evento.getClickCount() == 2 && tabela.rowAtPoint(evento.getPoint()) >= 0) {
                    acao.run();
                }
            }
        });
    }

    static void aoFechar(JInternalFrame janela, Runnable acao) {
        janela.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent evento) {
                acao.run();
            }
        });
    }

    static void fechar(JInternalFrame janela) {
        Container pai = janela.getParent();
        janela.dispose();
        if (pai != null && janela.getParent() == pai) {
            pai.remove(janela);
            pai.repaint();
        }
    }

    static void trazerParaFrente(JInternalFrame janela) {
        try {
            if (janela.isIcon()) {
                janela.setIcon(false);
            }
            janela.toFront();
            janela.setSelected(true);
        } catch (PropertyVetoException ignorada) {
        }
    }

    static void tituloEmNegrito(JComponent painel) {
        if (painel.getBorder() instanceof TitledBorder borda) {
            Font fonte = UIManager.getFont("TitledBorder.font");
            borda.setTitleFont((fonte != null ? fonte : painel.getFont()).deriveFont(Font.BOLD));
        }
    }

    static void mostrarSucesso(Component pai, String mensagem) {
        JOptionPane.showMessageDialog(pai, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    static void mostrarAviso(Component pai, String mensagem) {
        JOptionPane.showMessageDialog(pai, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    static void mostrarErro(Component pai, String mensagem) {
        JOptionPane.showMessageDialog(pai, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    static boolean confirmar(Component pai, String titulo, String mensagem) {
        Object[] opcoes = {"Sim", "Não"};
        int resposta = JOptionPane.showOptionDialog(pai, mensagem, titulo, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]);
        return resposta == 0;
    }

    private static final class CursorSomenteEdicao extends DefaultCaret {

        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics g) {
            if (getComponent().isEditable()) {
                super.paint(g);
            }
        }
    }
}
