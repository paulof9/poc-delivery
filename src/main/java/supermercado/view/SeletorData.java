package supermercado.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

final class SeletorData extends JPopupMenu {

    private static final long serialVersionUID = 1L;
    private static final Locale PT_BR = Locale.of("pt", "BR");
    private static final String[] DIAS_DA_SEMANA = {"D", "S", "T", "Q", "Q", "S", "S"};
    private static final int CELULAS_DE_DIAS = 42;

    private final transient Consumer<LocalDate> aoSelecionar;
    private final JLabel lblMes = new JLabel("", SwingConstants.CENTER);
    private final JPanel pnlDias = new JPanel(new GridLayout(0, 7, 2, 2));
    private YearMonth mesExibido = YearMonth.now();
    private LocalDate dataAtual;

    SeletorData(Consumer<LocalDate> aoSelecionar) {
        this.aoSelecionar = aoSelecionar;
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(6, 6, 6, 6)));

        lblMes.setFont(lblMes.getFont().deriveFont(Font.BOLD));
        JPanel cabecalho = new JPanel(new BorderLayout(6, 0));
        cabecalho.add(botaoNavegacao("<", "Mês anterior", -1), BorderLayout.WEST);
        cabecalho.add(lblMes, BorderLayout.CENTER);
        cabecalho.add(botaoNavegacao(">", "Próximo mês", 1), BorderLayout.EAST);

        JButton btnHoje = new JButton("Hoje");
        btnHoje.addActionListener(evento -> selecionar(LocalDate.now()));

        add(cabecalho, BorderLayout.NORTH);
        add(pnlDias, BorderLayout.CENTER);
        add(btnHoje, BorderLayout.SOUTH);
    }

    void exibir(Component campo, LocalDate data) {
        dataAtual = data;
        mesExibido = YearMonth.from(data != null ? data : LocalDate.now());
        montarDias();
        show(campo, 0, campo.getHeight());
    }

    private JButton botaoNavegacao(String texto, String dica, int meses) {
        JButton botao = new JButton(texto);
        botao.setToolTipText(dica);
        botao.setMargin(new Insets(2, 6, 2, 6));
        botao.addActionListener(evento -> {
            mesExibido = mesExibido.plusMonths(meses);
            montarDias();
        });
        return botao;
    }

    private void montarDias() {
        String nomeMes = mesExibido.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, PT_BR);
        lblMes.setText(Character.toUpperCase(nomeMes.charAt(0)) + nomeMes.substring(1) + " de " + mesExibido.getYear());

        pnlDias.removeAll();
        for (String dia : DIAS_DA_SEMANA) {
            pnlDias.add(new JLabel(dia, SwingConstants.CENTER));
        }
        int deslocamento = mesExibido.atDay(1).getDayOfWeek().getValue() % 7;
        for (int i = 0; i < deslocamento; i++) {
            pnlDias.add(new JLabel());
        }
        LocalDate hoje = LocalDate.now();
        for (int dia = 1; dia <= mesExibido.lengthOfMonth(); dia++) {
            LocalDate data = mesExibido.atDay(dia);
            JButton botao = new JButton(String.valueOf(dia));
            botao.setMargin(new Insets(2, 2, 2, 2));
            botao.setFocusable(false);
            if (data.equals(dataAtual) || (dataAtual == null && data.equals(hoje))) {
                botao.setFont(botao.getFont().deriveFont(Font.BOLD));
            }
            botao.addActionListener(evento -> selecionar(data));
            pnlDias.add(botao);
        }
        for (int i = deslocamento + mesExibido.lengthOfMonth(); i < CELULAS_DE_DIAS; i++) {
            pnlDias.add(new JLabel());
        }
        pnlDias.revalidate();
        pnlDias.repaint();
    }

    private void selecionar(LocalDate data) {
        setVisible(false);
        aoSelecionar.accept(data);
    }
}
