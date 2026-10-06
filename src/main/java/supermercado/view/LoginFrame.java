package supermercado.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class LoginFrame extends JFrame implements LoginView {

    private static final long serialVersionUID = 1L;

    private final JTextField txtIdentificacao = new JTextField(28);
    private final JPasswordField txtSenha = new JPasswordField(28);
    private final JButton btnEntrar = new JButton("Entrar");
    private final JButton btnFechar = new JButton("Fechar");
    private transient Runnable acaoEntrar;
    private transient Runnable acaoFechar;

    public LoginFrame() {
        setTitle("Sistema de Delivery - Autenticação");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        montarTela();

        btnEntrar.addActionListener(e -> UtilSwing.executar(acaoEntrar));
        btnFechar.addActionListener(e -> UtilSwing.executar(acaoFechar));
        txtIdentificacao.addActionListener(e -> txtSenha.requestFocusInWindow());
        getRootPane().setDefaultButton(btnEntrar);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) {
                UtilSwing.executar(acaoFechar);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void montarTela() {
        JPanel pnlAcesso = new JPanel(new GridBagLayout());
        pnlAcesso.setBorder(BorderFactory.createTitledBorder("Acesso ao Sistema"));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(6, 10, 2, 10);
        pnlAcesso.add(new JLabel("Usuário ou e-mail:"), c);
        c.insets = new Insets(0, 10, 6, 10);
        pnlAcesso.add(txtIdentificacao, c);
        c.insets = new Insets(6, 10, 2, 10);
        pnlAcesso.add(new JLabel("Senha:"), c);
        c.insets = new Insets(0, 10, 10, 10);
        pnlAcesso.add(txtSenha, c);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 4));
        pnlBotoes.add(btnEntrar);
        pnlBotoes.add(btnFechar);

        JPanel conteudo = new JPanel(new BorderLayout(0, 6));
        conteudo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        conteudo.add(pnlAcesso, BorderLayout.CENTER);
        conteudo.add(pnlBotoes, BorderLayout.SOUTH);
        setContentPane(conteudo);
    }

    @Override
    public void exibir() {
        setLocationRelativeTo(null);
        setVisible(true);
        focarIdentificacao();
    }

    @Override
    public void fechar() {
        dispose();
    }

    @Override
    public String getIdentificacao() {
        return txtIdentificacao.getText();
    }

    @Override
    public String getSenha() {
        return new String(txtSenha.getPassword());
    }

    @Override
    public void limparSenha() {
        txtSenha.setText("");
    }

    @Override
    public void focarIdentificacao() {
        txtIdentificacao.requestFocusInWindow();
    }

    @Override
    public void mostrarAviso(String mensagem) {
        UtilSwing.mostrarAviso(this, mensagem);
    }

    @Override
    public void mostrarErro(String mensagem) {
        UtilSwing.mostrarErro(this, mensagem);
    }

    @Override
    public void setAcaoEntrar(Runnable acao) {
        acaoEntrar = acao;
    }

    @Override
    public void setAcaoFechar(Runnable acao) {
        acaoFechar = acao;
    }
}
