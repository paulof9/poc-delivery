package supermercado.view;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class BuscaProdutoFrame extends javax.swing.JInternalFrame implements BuscaProdutoView {

    private static final long serialVersionUID = 1L;

    private final DefaultTableModel modeloProdutos;
    private transient Runnable acaoBuscar;
    private transient Runnable acaoNovo;
    private transient Runnable acaoVisualizar;
    private transient Runnable acaoSelecaoAlterada;
    private transient Runnable acaoFechar;

    public BuscaProdutoFrame() {
        initComponents();
        UtilSwing.tituloEmNegrito(pnlBuscar);
        modeloProdutos = UtilSwing.configurarTabela(tblProdutos,
                "Nome do produto", "Preço de custo", "Categoria", "Margem de lucro (%)", "Preço de venda");
        UtilSwing.definirLarguras(tblProdutos, 240, 100, 130, 130, 110);
        UtilSwing.alinharColunas(tblProdutos, SwingConstants.RIGHT, 1, 3, 4);

        btnBuscar.addActionListener(e -> UtilSwing.executar(acaoBuscar));
        txtBusca.addActionListener(e -> UtilSwing.executar(acaoBuscar));
        btnNovo.addActionListener(e -> UtilSwing.executar(acaoNovo));
        btnVisualizar.addActionListener(e -> UtilSwing.executar(acaoVisualizar));
        btnFechar.addActionListener(e -> UtilSwing.executar(acaoFechar));
        UtilSwing.aoSelecionarLinha(tblProdutos, () -> UtilSwing.executar(acaoSelecaoAlterada));
        UtilSwing.aoDuploClique(tblProdutos, () -> {
            if (btnVisualizar.isEnabled()) {
                UtilSwing.executar(acaoVisualizar);
            }
        });
        UtilSwing.aoFechar(this, () -> UtilSwing.executar(acaoFechar));
    }

    @Override
    public void setCriteriosBusca(List<String> criterios) {
        cmbBuscaPor.setModel(new DefaultComboBoxModel<>(criterios.toArray(String[]::new)));
    }

    @Override
    public int getCriterioSelecionado() {
        return cmbBuscaPor.getSelectedIndex();
    }

    @Override
    public String getTextoBusca() {
        return txtBusca.getText();
    }

    @Override
    public void setProdutos(List<String[]> linhas) {
        UtilSwing.substituirLinhas(modeloProdutos, linhas);
    }

    @Override
    public int getLinhaSelecionada() {
        return UtilSwing.linhaSelecionada(tblProdutos);
    }

    @Override
    public void selecionarLinha(int linha) {
        UtilSwing.selecionarLinha(tblProdutos, linha);
    }

    @Override
    public void setVisualizarHabilitado(boolean habilitado) {
        btnVisualizar.setEnabled(habilitado);
    }

    @Override
    public void setAcaoBuscar(Runnable acao) {
        acaoBuscar = acao;
    }

    @Override
    public void setAcaoNovo(Runnable acao) {
        acaoNovo = acao;
    }

    @Override
    public void setAcaoVisualizar(Runnable acao) {
        acaoVisualizar = acao;
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

    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlBuscar = new javax.swing.JPanel();
        lblBuscaPor = new javax.swing.JLabel();
        cmbBuscaPor = new javax.swing.JComboBox<>();
        txtBusca = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        scrProdutos = new javax.swing.JScrollPane();
        tblProdutos = new javax.swing.JTable();
        sepBotoes = new javax.swing.JSeparator();
        btnNovo = new javax.swing.JButton();
        btnVisualizar = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Buscar Produtos");

        pnlBuscar.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar"));
        lblBuscaPor.setText("Busca por");

        btnBuscar.setText("Buscar");

        javax.swing.GroupLayout pnlBuscarLayout = new javax.swing.GroupLayout(pnlBuscar);
        pnlBuscar.setLayout(pnlBuscarLayout);
        pnlBuscarLayout.setHorizontalGroup(
            pnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBuscaPor)
                    .addGroup(pnlBuscarLayout.createSequentialGroup()
                        .addComponent(cmbBuscaPor, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBusca, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlBuscarLayout.setVerticalGroup(
            pnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscaPor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbBuscaPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrProdutos.setViewportView(tblProdutos);

        btnNovo.setText("Novo");

        btnVisualizar.setText("Visualizar");
        btnVisualizar.setEnabled(false);

        btnFechar.setText("Fechar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrProdutos, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(sepBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnVisualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrProdutos, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sepBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNovo)
                    .addComponent(btnVisualizar)
                    .addComponent(btnFechar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnVisualizar;
    private javax.swing.JComboBox<String> cmbBuscaPor;
    private javax.swing.JLabel lblBuscaPor;
    private javax.swing.JPanel pnlBuscar;
    private javax.swing.JScrollPane scrProdutos;
    private javax.swing.JSeparator sepBotoes;
    private javax.swing.JTable tblProdutos;
    private javax.swing.JTextField txtBusca;
    // End of variables declaration//GEN-END:variables
}
