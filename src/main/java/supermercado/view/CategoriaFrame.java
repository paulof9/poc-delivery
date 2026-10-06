package supermercado.view;

import java.util.List;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class CategoriaFrame extends javax.swing.JInternalFrame implements CategoriaView {

    private static final long serialVersionUID = 1L;

    private final DefaultTableModel modeloCategorias;
    private transient Runnable acaoNovo;
    private transient Runnable acaoEditar;
    private transient Runnable acaoExcluir;
    private transient Runnable acaoSalvar;
    private transient Runnable acaoCancelar;
    private transient Runnable acaoSelecaoAlterada;
    private transient Runnable acaoFechar;

    public CategoriaFrame() {
        initComponents();
        UtilSwing.tituloEmNegrito(pnlDetalhes);
        UtilSwing.tituloEmNegrito(pnlCadastradas);
        modeloCategorias = UtilSwing.configurarTabela(tblCategorias, "Categoria", "Percentual de lucro (%)");
        UtilSwing.alinharColunas(tblCategorias, SwingConstants.CENTER, 1);

        btnNovo.addActionListener(e -> UtilSwing.executar(acaoNovo));
        btnEditar.addActionListener(e -> UtilSwing.executar(acaoEditar));
        btnExcluir.addActionListener(e -> UtilSwing.executar(acaoExcluir));
        btnSalvar.addActionListener(e -> UtilSwing.executar(acaoSalvar));
        btnCancelar.addActionListener(e -> UtilSwing.executar(acaoCancelar));
        btnFechar.addActionListener(e -> UtilSwing.executar(acaoFechar));
        txtPercentual.addActionListener(e -> {
            if (btnSalvar.isEnabled()) {
                UtilSwing.executar(acaoSalvar);
            }
        });
        UtilSwing.aoSelecionarLinha(tblCategorias, () -> UtilSwing.executar(acaoSelecaoAlterada));
        UtilSwing.aoFechar(this, () -> UtilSwing.executar(acaoFechar));
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
    public String getPercentual() {
        return txtPercentual.getText();
    }

    @Override
    public void setPercentual(String percentual) {
        txtPercentual.setText(percentual);
    }

    @Override
    public void setModo(String modo) {
        lblModo.setText(modo);
    }

    @Override
    public void setCamposEditaveis(boolean editaveis) {
        UtilSwing.definirSomenteLeitura(!editaveis, txtNome, txtPercentual);
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
        tblCategorias.setEnabled(habilitada);
    }

    @Override
    public void setFechamentoPermitido(boolean permitido) {
        setClosable(permitido);
    }

    @Override
    public void setCategorias(List<String[]> linhas) {
        UtilSwing.substituirLinhas(modeloCategorias, linhas);
    }

    @Override
    public int getLinhaSelecionada() {
        return UtilSwing.linhaSelecionada(tblCategorias);
    }

    @Override
    public void selecionarLinha(int linha) {
        UtilSwing.selecionarLinha(tblCategorias, linha);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDetalhes = new javax.swing.JPanel();
        lblModo = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblPercentual = new javax.swing.JLabel();
        txtPercentual = new javax.swing.JTextField();
        btnNovo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        pnlCadastradas = new javax.swing.JPanel();
        scrCategorias = new javax.swing.JScrollPane();
        tblCategorias = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Categorias de Produtos");

        pnlDetalhes.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalhes da Categoria"));
        lblModo.setText("Modo: Visualização");

        lblNome.setText("Categoria:");

        txtNome.setEditable(false);

        lblPercentual.setText("Percentual de lucro (%):");

        txtPercentual.setEditable(false);

        btnNovo.setText("Novo");

        btnEditar.setText("Editar");

        btnExcluir.setText("Excluir");

        btnSalvar.setText("Salvar");
        btnSalvar.setEnabled(false);

        btnCancelar.setText("Cancelar");
        btnCancelar.setEnabled(false);

        btnFechar.setText("Fechar");

        javax.swing.GroupLayout pnlDetalhesLayout = new javax.swing.GroupLayout(pnlDetalhes);
        pnlDetalhes.setLayout(pnlDetalhesLayout);
        pnlDetalhesLayout.setHorizontalGroup(
            pnlDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalhesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblModo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlDetalhesLayout.createSequentialGroup()
                        .addGroup(pnlDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNome)
                            .addComponent(lblPercentual))
                        .addGap(30, 30, 30)
                        .addGroup(pnlDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNome, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                            .addComponent(txtPercentual, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlDetalhesLayout.createSequentialGroup()
                        .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlDetalhesLayout.setVerticalGroup(
            pnlDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalhesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblModo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPercentual)
                    .addComponent(txtPercentual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNovo)
                    .addComponent(btnEditar)
                    .addComponent(btnExcluir)
                    .addComponent(btnSalvar)
                    .addComponent(btnCancelar)
                    .addComponent(btnFechar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCadastradas.setBorder(javax.swing.BorderFactory.createTitledBorder("Categorias cadastradas"));
        scrCategorias.setViewportView(tblCategorias);

        javax.swing.GroupLayout pnlCadastradasLayout = new javax.swing.GroupLayout(pnlCadastradas);
        pnlCadastradas.setLayout(pnlCadastradasLayout);
        pnlCadastradasLayout.setHorizontalGroup(
            pnlCadastradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCadastradasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlCadastradasLayout.setVerticalGroup(
            pnlCadastradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCadastradasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDetalhes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCadastradas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCadastradas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JLabel lblModo;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPercentual;
    private javax.swing.JPanel pnlCadastradas;
    private javax.swing.JPanel pnlDetalhes;
    private javax.swing.JScrollPane scrCategorias;
    private javax.swing.JTable tblCategorias;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtPercentual;
    // End of variables declaration//GEN-END:variables
}
