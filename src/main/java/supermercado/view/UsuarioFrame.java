package supermercado.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class UsuarioFrame extends JInternalFrame implements UsuarioView {

    private static final long serialVersionUID = 1L;

    private final JTextField txtNomeCompleto = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtNomeUsuario = new JTextField(18);
    private final JPasswordField txtSenha = new JPasswordField(18);
    private final JPasswordField txtConfirmacao = new JPasswordField(18);
    private final JButton btnVerSenha = new JButton("Mostrar");
    private final JButton btnVerConfirmacao = new JButton("Mostrar");
    private final JComboBox<String> cmbPerfil = new JComboBox<>();
    private final JTextField txtStatus = new JTextField();
    private final JComboBox<String> cmbCliente = new JComboBox<>();
    private final JButton btnIncluirCliente = new JButton("Incluir cliente");
    private final JLabel lblModo = new JLabel("Modo: Visualização", SwingConstants.RIGHT);
    private final JButton btnNovo = new JButton("Novo");
    private final JButton btnEditar = new JButton("Editar");
    private final JButton btnExcluir = new JButton("Excluir");
    private final JButton btnHabilitar = new JButton("Habilitar");
    private final JButton btnDesabilitar = new JButton("Desabilitar");
    private final JButton btnSalvar = new JButton("Salvar");
    private final JButton btnCancelar = new JButton("Cancelar");
    private final JButton btnFechar = new JButton("Fechar");
    private final JTable tblUsuarios = new JTable();
    private final DefaultTableModel modeloUsuarios;
    private final char mascaraSenha;
    private transient Runnable acaoNovo;
    private transient Runnable acaoEditar;
    private transient Runnable acaoExcluir;
    private transient Runnable acaoHabilitar;
    private transient Runnable acaoDesabilitar;
    private transient Runnable acaoSalvar;
    private transient Runnable acaoCancelar;
    private transient Runnable acaoSelecaoAlterada;
    private transient Runnable acaoPerfilAlterado;
    private transient Runnable acaoIncluirCliente;
    private transient Runnable acaoFechar;

    public UsuarioFrame() {
        super("Usuários", true, true, true, true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mascaraSenha = txtSenha.getEchoChar();
        modeloUsuarios = UtilSwing.configurarTabela(tblUsuarios, "Nome completo", "E-mail", "Usuário",
                "Perfil", "Cliente associado", "Status");
        UtilSwing.definirLarguras(tblUsuarios, 190, 200, 100, 100, 150, 90);
        UtilSwing.definirSomenteLeitura(true, txtStatus);
        cmbCliente.setPrototypeDisplayValue("Nome de cliente com tamanho suficiente para leitura");
        montarTela();

        btnNovo.addActionListener(e -> UtilSwing.executar(acaoNovo));
        btnEditar.addActionListener(e -> UtilSwing.executar(acaoEditar));
        btnExcluir.addActionListener(e -> UtilSwing.executar(acaoExcluir));
        btnHabilitar.addActionListener(e -> UtilSwing.executar(acaoHabilitar));
        btnDesabilitar.addActionListener(e -> UtilSwing.executar(acaoDesabilitar));
        btnSalvar.addActionListener(e -> UtilSwing.executar(acaoSalvar));
        btnCancelar.addActionListener(e -> UtilSwing.executar(acaoCancelar));
        btnFechar.addActionListener(e -> UtilSwing.executar(acaoFechar));
        btnIncluirCliente.addActionListener(e -> UtilSwing.executar(acaoIncluirCliente));
        btnVerSenha.addActionListener(e -> alternarVisibilidade(txtSenha, btnVerSenha));
        btnVerConfirmacao.addActionListener(e -> alternarVisibilidade(txtConfirmacao, btnVerConfirmacao));
        cmbPerfil.addActionListener(e -> UtilSwing.executar(acaoPerfilAlterado));
        UtilSwing.aoSelecionarLinha(tblUsuarios, () -> UtilSwing.executar(acaoSelecaoAlterada));
        UtilSwing.aoFechar(this, () -> UtilSwing.executar(acaoFechar));

        setSize(1000, 640);
    }

    private void montarTela() {
        JPanel pnlCampos = new JPanel(new GridBagLayout());
        adicionarCampo(pnlCampos, 0, 0, "Nome completo:", txtNomeCompleto, 5);
        adicionarCampo(pnlCampos, 0, 1, "E-mail:", txtEmail, 5);
        adicionarCampo(pnlCampos, 0, 2, "Nome de usuário:", txtNomeUsuario, 1);
        adicionarCampo(pnlCampos, 0, 3, "Senha:", txtSenha, 1);
        pnlCampos.add(btnVerSenha, restricoes(2, 3));
        adicionarCampo(pnlCampos, 3, 3, "Confirmar senha:", txtConfirmacao, 1);
        pnlCampos.add(btnVerConfirmacao, restricoes(5, 3));
        adicionarCampo(pnlCampos, 0, 4, "Perfil do usuário:", cmbPerfil, 2);
        adicionarCampo(pnlCampos, 3, 4, "Status:", txtStatus, 2);
        adicionarCampo(pnlCampos, 0, 5, "Cliente associado:", cmbCliente, 4);
        pnlCampos.add(btnIncluirCliente, restricoes(5, 5));

        JPanel pnlBotoes = new JPanel(new GridLayout(1, 8, 8, 0));
        for (JButton botao : new JButton[]{btnNovo, btnEditar, btnExcluir, btnHabilitar, btnDesabilitar,
            btnSalvar, btnCancelar, btnFechar}) {
            pnlBotoes.add(botao);
        }

        JPanel pnlDetalhes = new JPanel(new BorderLayout(0, 8));
        pnlDetalhes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Detalhes do Usuário"),
                BorderFactory.createEmptyBorder(0, 6, 6, 6)));
        UtilSwing.tituloEmNegrito(pnlDetalhes);
        pnlDetalhes.add(lblModo, BorderLayout.NORTH);
        pnlDetalhes.add(pnlCampos, BorderLayout.CENTER);
        pnlDetalhes.add(pnlBotoes, BorderLayout.SOUTH);

        JPanel pnlCadastrados = new JPanel(new BorderLayout());
        pnlCadastrados.setBorder(BorderFactory.createTitledBorder("Usuários cadastrados"));
        UtilSwing.tituloEmNegrito(pnlCadastrados);
        pnlCadastrados.add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);

        JPanel conteudo = new JPanel(new BorderLayout(0, 8));
        conteudo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        conteudo.add(pnlDetalhes, BorderLayout.NORTH);
        conteudo.add(pnlCadastrados, BorderLayout.CENTER);
        setContentPane(conteudo);
    }

    private static void adicionarCampo(JPanel painel, int coluna, int linha, String rotulo,
            JComponent campo, int largura) {
        painel.add(new JLabel(rotulo), restricoes(coluna, linha));
        GridBagConstraints c = restricoes(coluna + 1, linha);
        c.gridwidth = largura;
        c.weightx = 1;
        painel.add(campo, c);
    }

    private static GridBagConstraints restricoes(int coluna, int linha) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = coluna;
        c.gridy = linha;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(3, 6, 3, 6);
        return c;
    }

    private void alternarVisibilidade(JPasswordField campo, JButton botao) {
        boolean oculto = campo.getEchoChar() != 0;
        campo.setEchoChar(oculto ? (char) 0 : mascaraSenha);
        botao.setText(oculto ? "Ocultar" : "Mostrar");
    }

    private void ocultarSenhas() {
        txtSenha.setEchoChar(mascaraSenha);
        txtConfirmacao.setEchoChar(mascaraSenha);
        btnVerSenha.setText("Mostrar");
        btnVerConfirmacao.setText("Mostrar");
    }

    @Override
    public String getNomeCompleto() {
        return txtNomeCompleto.getText();
    }

    @Override
    public void setNomeCompleto(String nome) {
        txtNomeCompleto.setText(nome);
    }

    @Override
    public String getEmail() {
        return txtEmail.getText();
    }

    @Override
    public void setEmail(String email) {
        txtEmail.setText(email);
    }

    @Override
    public String getNomeUsuario() {
        return txtNomeUsuario.getText();
    }

    @Override
    public void setNomeUsuario(String nomeUsuario) {
        txtNomeUsuario.setText(nomeUsuario);
    }

    @Override
    public String getSenha() {
        return new String(txtSenha.getPassword());
    }

    @Override
    public String getConfirmacaoSenha() {
        return new String(txtConfirmacao.getPassword());
    }

    @Override
    public void setSenhas(String senha) {
        txtSenha.setText(senha);
        txtConfirmacao.setText(senha);
    }

    @Override
    public void setPerfis(List<String> perfis) {
        cmbPerfil.setModel(new DefaultComboBoxModel<>(perfis.toArray(String[]::new)));
    }

    @Override
    public String getPerfil() {
        return (String) cmbPerfil.getSelectedItem();
    }

    @Override
    public void setPerfil(String perfil) {
        cmbPerfil.setSelectedItem(perfil);
    }

    @Override
    public void setStatus(String status) {
        txtStatus.setText(status);
    }

    @Override
    public void setClientes(List<String> nomes) {
        cmbCliente.setModel(new DefaultComboBoxModel<>(nomes.toArray(String[]::new)));
        cmbCliente.setSelectedIndex(-1);
    }

    @Override
    public int getClienteSelecionado() {
        return cmbCliente.getSelectedIndex();
    }

    @Override
    public void setClienteSelecionado(int indice) {
        cmbCliente.setSelectedIndex(indice >= 0 && indice < cmbCliente.getItemCount() ? indice : -1);
    }

    @Override
    public void setModo(String modo) {
        lblModo.setText(modo);
    }

    @Override
    public void setCamposEditaveis(boolean editaveis) {
        UtilSwing.definirSomenteLeitura(!editaveis, txtNomeCompleto, txtEmail, txtNomeUsuario,
                txtSenha, txtConfirmacao);
        btnVerSenha.setEnabled(editaveis);
        btnVerConfirmacao.setEnabled(editaveis);
        if (!editaveis) {
            ocultarSenhas();
        }
    }

    @Override
    public void setPerfilEditavel(boolean editavel) {
        cmbPerfil.setEnabled(editavel);
    }

    @Override
    public void setClienteEditavel(boolean editavel) {
        cmbCliente.setEnabled(editavel);
        btnIncluirCliente.setEnabled(editavel);
    }

    @Override
    public void setBotoesHabilitados(boolean novo, boolean editar, boolean excluir, boolean habilitar,
            boolean desabilitar, boolean salvar, boolean cancelar, boolean fechar) {
        btnNovo.setEnabled(novo);
        btnEditar.setEnabled(editar);
        btnExcluir.setEnabled(excluir);
        btnHabilitar.setEnabled(habilitar);
        btnDesabilitar.setEnabled(desabilitar);
        btnSalvar.setEnabled(salvar);
        btnCancelar.setEnabled(cancelar);
        btnFechar.setEnabled(fechar);
    }

    @Override
    public void setTabelaHabilitada(boolean habilitada) {
        tblUsuarios.setEnabled(habilitada);
    }

    @Override
    public void setFechamentoPermitido(boolean permitido) {
        setClosable(permitido);
    }

    @Override
    public void setUsuarios(List<String[]> linhas) {
        UtilSwing.substituirLinhas(modeloUsuarios, linhas);
    }

    @Override
    public int getLinhaSelecionada() {
        return UtilSwing.linhaSelecionada(tblUsuarios);
    }

    @Override
    public void selecionarLinha(int linha) {
        UtilSwing.selecionarLinha(tblUsuarios, linha);
    }

    @Override
    public void focarNome() {
        txtNomeCompleto.requestFocusInWindow();
    }

    @Override
    public void setAcaoNovo(Runnable acao) {
        acaoNovo = acao;
    }

    @Override
    public void setAcaoEditar(Runnable acao) {
        acaoEditar = acao;
    }

    @Override
    public void setAcaoExcluir(Runnable acao) {
        acaoExcluir = acao;
    }

    @Override
    public void setAcaoHabilitar(Runnable acao) {
        acaoHabilitar = acao;
    }

    @Override
    public void setAcaoDesabilitar(Runnable acao) {
        acaoDesabilitar = acao;
    }

    @Override
    public void setAcaoSalvar(Runnable acao) {
        acaoSalvar = acao;
    }

    @Override
    public void setAcaoCancelar(Runnable acao) {
        acaoCancelar = acao;
    }

    @Override
    public void setAcaoSelecaoAlterada(Runnable acao) {
        acaoSelecaoAlterada = acao;
    }

    @Override
    public void setAcaoPerfilAlterado(Runnable acao) {
        acaoPerfilAlterado = acao;
    }

    @Override
    public void setAcaoIncluirCliente(Runnable acao) {
        acaoIncluirCliente = acao;
    }

    @Override
    public void fechar() {
        UtilSwing.fechar(this);
    }

    @Override
    public boolean estaAberta() {
        return !isClosed();
    }

    @Override
    public void trazerParaFrente() {
        UtilSwing.trazerParaFrente(this);
    }

    @Override
    public void setAcaoFechar(Runnable acao) {
        acaoFechar = acao;
    }

    @Override
    public void mostrarSucesso(String mensagem) {
        UtilSwing.mostrarSucesso(this, mensagem);
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
    public boolean confirmar(String titulo, String mensagem) {
        return UtilSwing.confirmar(this, titulo, mensagem);
    }
}
