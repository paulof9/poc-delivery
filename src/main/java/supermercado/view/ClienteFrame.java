package supermercado.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class ClienteFrame extends JInternalFrame implements ClienteView {

    private static final long serialVersionUID = 1L;

    private final JTextField txtNome = new JTextField();
    private final JTextField txtLogradouro = new JTextField();
    private final JTextField txtBairro = new JTextField();
    private final JTextField txtCidade = new JTextField();
    private final JTextField txtUf = new JTextField(4);
    private final JTextField txtTipo = new JTextField();
    private final JTextField txtTotal = new JTextField();
    private final JLabel lblModo = new JLabel("Modo: Visualização", SwingConstants.RIGHT);
    private final JButton btnNovo = new JButton("Novo");
    private final JButton btnEditar = new JButton("Editar");
    private final JButton btnExcluir = new JButton("Excluir");
    private final JButton btnSalvar = new JButton("Salvar");
    private final JButton btnCancelar = new JButton("Cancelar");
    private final JButton btnFechar = new JButton("Fechar");
    private final JTable tblClientes = new JTable();
    private final DefaultTableModel modeloClientes;
    private transient Runnable acaoNovo;
    private transient Runnable acaoEditar;
    private transient Runnable acaoExcluir;
    private transient Runnable acaoSalvar;
    private transient Runnable acaoCancelar;
    private transient Runnable acaoSelecaoAlterada;
    private transient Runnable acaoFechar;

    public ClienteFrame() {
        super("Clientes", true, true, true, true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        modeloClientes = UtilSwing.configurarTabela(tblClientes, "Nome", "Cidade", "UF", "Bairro",
                "Tipo de cliente", "Total de compras (R$)");
        UtilSwing.definirLarguras(tblClientes, 200, 120, 40, 140, 110, 140);
        UtilSwing.alinharColunas(tblClientes, SwingConstants.CENTER, 2, 4);
        UtilSwing.alinharColunas(tblClientes, SwingConstants.RIGHT, 5);
        UtilSwing.definirSomenteLeitura(true, txtTipo, txtTotal);
        txtTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        montarTela();

        btnNovo.addActionListener(e -> UtilSwing.executar(acaoNovo));
        btnEditar.addActionListener(e -> UtilSwing.executar(acaoEditar));
        btnExcluir.addActionListener(e -> UtilSwing.executar(acaoExcluir));
        btnSalvar.addActionListener(e -> UtilSwing.executar(acaoSalvar));
        btnCancelar.addActionListener(e -> UtilSwing.executar(acaoCancelar));
        btnFechar.addActionListener(e -> UtilSwing.executar(acaoFechar));
        UtilSwing.aoSelecionarLinha(tblClientes, () -> UtilSwing.executar(acaoSelecaoAlterada));
        UtilSwing.aoFechar(this, () -> UtilSwing.executar(acaoFechar));

        setSize(860, 580);
    }

    private void montarTela() {
        JPanel pnlEndereco = new JPanel(new GridBagLayout());
        pnlEndereco.setBorder(BorderFactory.createTitledBorder("Endereço"));
        UtilSwing.tituloEmNegrito(pnlEndereco);
        adicionarLinha(pnlEndereco, 0, "Logradouro:", txtLogradouro, "Bairro:", txtBairro);
        adicionarLinha(pnlEndereco, 1, "Cidade:", txtCidade, "UF:", txtUf);

        JPanel pnlCampos = new JPanel(new GridBagLayout());
        GridBagConstraints c = restricoes(0, 0);
        pnlCampos.add(new JLabel("Nome:"), c);
        c = restricoes(1, 0);
        c.gridwidth = 3;
        c.weightx = 1;
        pnlCampos.add(txtNome, c);
        c = restricoes(0, 1);
        c.gridwidth = 4;
        c.weightx = 1;
        pnlCampos.add(pnlEndereco, c);
        adicionarLinha(pnlCampos, 2, "Tipo de cliente:", txtTipo, "Total de compras (R$):", txtTotal);

        JPanel pnlBotoes = new JPanel(new GridLayout(1, 6, 10, 0));
        for (JButton botao : new JButton[]{btnNovo, btnEditar, btnExcluir, btnSalvar, btnCancelar, btnFechar}) {
            pnlBotoes.add(botao);
        }

        JPanel pnlDetalhes = new JPanel(new BorderLayout(0, 8));
        pnlDetalhes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Detalhes do Cliente"),
                BorderFactory.createEmptyBorder(0, 6, 6, 6)));
        UtilSwing.tituloEmNegrito(pnlDetalhes);
        pnlDetalhes.add(lblModo, BorderLayout.NORTH);
        pnlDetalhes.add(pnlCampos, BorderLayout.CENTER);
        pnlDetalhes.add(pnlBotoes, BorderLayout.SOUTH);

        JPanel pnlCadastrados = new JPanel(new BorderLayout());
        pnlCadastrados.setBorder(BorderFactory.createTitledBorder("Clientes cadastrados"));
        UtilSwing.tituloEmNegrito(pnlCadastrados);
        pnlCadastrados.add(new JScrollPane(tblClientes), BorderLayout.CENTER);

        JPanel conteudo = new JPanel(new BorderLayout(0, 8));
        conteudo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        conteudo.add(pnlDetalhes, BorderLayout.NORTH);
        conteudo.add(pnlCadastrados, BorderLayout.CENTER);
        setContentPane(conteudo);
    }

    private static void adicionarLinha(JPanel painel, int linha, String rotulo1, JComponent campo1,
            String rotulo2, JComponent campo2) {
        painel.add(new JLabel(rotulo1), restricoes(0, linha));
        GridBagConstraints c = restricoes(1, linha);
        c.weightx = 1;
        painel.add(campo1, c);
        painel.add(new JLabel(rotulo2), restricoes(2, linha));
        c = restricoes(3, linha);
        c.weightx = 0.5;
        painel.add(campo2, c);
    }

    private static GridBagConstraints restricoes(int coluna, int linha) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = coluna;
        c.gridy = linha;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(4, 6, 4, 6);
        return c;
    }

    @Override
    public String getNome() {
        return txtNome.getText();
    }

    @Override
    public void setNome(String nome) {
        txtNome.setText(nome);
    }

    @Override
    public String getLogradouro() {
        return txtLogradouro.getText();
    }

    @Override
    public void setLogradouro(String logradouro) {
        txtLogradouro.setText(logradouro);
    }

    @Override
    public String getBairro() {
        return txtBairro.getText();
    }

    @Override
    public void setBairro(String bairro) {
        txtBairro.setText(bairro);
    }

    @Override
    public String getCidade() {
        return txtCidade.getText();
    }

    @Override
    public void setCidade(String cidade) {
        txtCidade.setText(cidade);
    }

    @Override
    public String getUf() {
        return txtUf.getText();
    }

    @Override
    public void setUf(String uf) {
        txtUf.setText(uf);
    }

    @Override
    public void setTipoCliente(String tipo) {
        txtTipo.setText(tipo);
    }

    @Override
    public void setTotalCompras(String total) {
        txtTotal.setText(total);
    }

    @Override
    public void setModo(String modo) {
        lblModo.setText(modo);
    }

    @Override
    public void setCamposEditaveis(boolean editaveis) {
        UtilSwing.definirSomenteLeitura(!editaveis, txtNome, txtLogradouro, txtBairro, txtCidade, txtUf);
    }

    @Override
    public void setBotoesHabilitados(boolean novo, boolean editar, boolean excluir,
            boolean salvar, boolean cancelar, boolean fechar) {
        btnNovo.setEnabled(novo);
        btnEditar.setEnabled(editar);
        btnExcluir.setEnabled(excluir);
        btnSalvar.setEnabled(salvar);
        btnCancelar.setEnabled(cancelar);
        btnFechar.setEnabled(fechar);
    }

    @Override
    public void setTabelaHabilitada(boolean habilitada) {
        tblClientes.setEnabled(habilitada);
    }

    @Override
    public void setFechamentoPermitido(boolean permitido) {
        setClosable(permitido);
    }

    @Override
    public void setClientes(List<String[]> linhas) {
        UtilSwing.substituirLinhas(modeloClientes, linhas);
    }

    @Override
    public int getLinhaSelecionada() {
        return UtilSwing.linhaSelecionada(tblClientes);
    }

    @Override
    public void selecionarLinha(int linha) {
        UtilSwing.selecionarLinha(tblClientes, linha);
    }

    @Override
    public void focarNome() {
        txtNome.requestFocusInWindow();
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
