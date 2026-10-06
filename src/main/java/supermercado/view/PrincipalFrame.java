package supermercado.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class PrincipalFrame extends JFrame implements PrincipalView {

    private static final long serialVersionUID = 1L;

    private final JDesktopPane desktop = new JDesktopPane();
    private final JMenu mnuOperacao = new JMenu("Operação");
    private final JMenu mnuDados = new JMenu("Dados");
    private final JMenu mnuUsuarios = new JMenu("Usuários");
    private final JMenuItem mniPedidos = new JMenuItem("Pedidos");
    private final JMenuItem mniCalcularMargem = new JMenuItem("Calcular preço de venda");
    private final JMenuItem mniProdutos = new JMenuItem("Produtos");
    private final JMenuItem mniCategorias = new JMenuItem("Categorias");
    private final JMenuItem mniClientes = new JMenuItem("Clientes");
    private final JMenuItem mniEntregadores = new JMenuItem("Entregadores");
    private final JMenuItem mniTaxasEntrega = new JMenuItem("Taxas de entrega");
    private final JMenuItem mniManterUsuarios = new JMenuItem("Manter usuários");
    private final JLabel lblUsuario = new JLabel();
    private final JButton btnSair = new JButton("Sair");
    private final JPanel pnlAcessoRapido = new JPanel(new GridLayout(1, 4, 10, 0));
    private final JButton btnNovoProduto = new JButton("Novo produto");
    private final JButton btnBuscarProdutos = new JButton("Buscar produtos");
    private final JButton btnClientes = new JButton("Clientes");
    private final JButton btnPrecoVenda = new JButton("Calcular preço de venda");
    private transient Runnable acaoIncluirProdutos;
    private transient Runnable acaoBuscarProdutos;
    private transient Runnable acaoCategorias;
    private transient Runnable acaoCalcularMargem;
    private transient Runnable acaoClientes;
    private transient Runnable acaoUsuarios;
    private transient Runnable acaoSair;

    public PrincipalFrame() {
        setTitle("POC Delivery");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        montarMenus();
        montarTela();

        mniCalcularMargem.addActionListener(e -> UtilSwing.executar(acaoCalcularMargem));
        mniProdutos.addActionListener(e -> UtilSwing.executar(acaoBuscarProdutos));
        mniCategorias.addActionListener(e -> UtilSwing.executar(acaoCategorias));
        mniClientes.addActionListener(e -> UtilSwing.executar(acaoClientes));
        mniManterUsuarios.addActionListener(e -> UtilSwing.executar(acaoUsuarios));
        btnNovoProduto.addActionListener(e -> UtilSwing.executar(acaoIncluirProdutos));
        btnBuscarProdutos.addActionListener(e -> UtilSwing.executar(acaoBuscarProdutos));
        btnClientes.addActionListener(e -> UtilSwing.executar(acaoClientes));
        btnPrecoVenda.addActionListener(e -> UtilSwing.executar(acaoCalcularMargem));
        btnSair.addActionListener(e -> UtilSwing.executar(acaoSair));

        setSize(1100, 750);
        setLocationRelativeTo(null);
    }

    private void montarMenus() {
        mniPedidos.setEnabled(false);
        mniEntregadores.setEnabled(false);
        mniTaxasEntrega.setEnabled(false);

        mnuOperacao.add(mniPedidos);
        mnuOperacao.add(mniCalcularMargem);

        mnuDados.add(mniProdutos);
        mnuDados.add(mniCategorias);
        mnuDados.add(mniClientes);
        mnuDados.add(mniEntregadores);
        mnuDados.add(mniTaxasEntrega);

        mnuUsuarios.add(mniManterUsuarios);

        JMenuBar barra = new JMenuBar();
        barra.add(mnuOperacao);
        barra.add(mnuDados);
        barra.add(mnuUsuarios);
        setJMenuBar(barra);
    }

    private void montarTela() {
        JLabel lblTitulo = new JLabel("Bem-vindo ao POC Delivery");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 26f));
        JLabel lblSubtitulo = new JLabel("Gerencie produtos, categorias, clientes e usuários do delivery.");
        lblSubtitulo.setFont(lblSubtitulo.getFont().deriveFont(14f));

        JPanel pnlTitulo = new JPanel();
        pnlTitulo.setOpaque(false);
        pnlTitulo.setLayout(new BoxLayout(pnlTitulo, BoxLayout.Y_AXIS));
        pnlTitulo.add(lblTitulo);
        pnlTitulo.add(lblSubtitulo);

        JPanel pnlUsuario = new JPanel(new BorderLayout(0, 6));
        pnlUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 210)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        pnlUsuario.add(lblUsuario, BorderLayout.CENTER);
        JPanel pnlSair = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlSair.setOpaque(false);
        pnlSair.add(btnSair);
        pnlUsuario.add(pnlSair, BorderLayout.SOUTH);

        for (JButton botao : new JButton[]{btnNovoProduto, btnBuscarProdutos, btnClientes, btnPrecoVenda}) {
            botao.setPreferredSize(new Dimension(180, 34));
            pnlAcessoRapido.add(botao);
        }
        pnlAcessoRapido.setBorder(BorderFactory.createTitledBorder("Acesso rápido"));
        UtilSwing.tituloEmNegrito(pnlAcessoRapido);

        JPanel pnlCabecalho = new JPanel(new BorderLayout(20, 10));
        pnlCabecalho.setBorder(BorderFactory.createEmptyBorder(14, 20, 10, 20));
        pnlCabecalho.add(pnlTitulo, BorderLayout.CENTER);
        pnlCabecalho.add(pnlUsuario, BorderLayout.EAST);
        pnlCabecalho.add(pnlAcessoRapido, BorderLayout.SOUTH);

        desktop.setBackground(new Color(192, 196, 200));

        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.add(pnlCabecalho, BorderLayout.NORTH);
        conteudo.add(desktop, BorderLayout.CENTER);
        setContentPane(conteudo);
    }

    @Override
    public void exibir() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    @Override
    public void fecharSessao() {
        dispose();
    }

    @Override
    public void abrirJanela(JanelaView janela) {
        if (!(janela instanceof JInternalFrame frame)) {
            throw new IllegalArgumentException("A janela precisa ser um JInternalFrame.");
        }
        desktop.add(frame);
        Dimension area = desktop.getSize();
        Dimension tamanho = frame.getSize();
        if (area.width > 0 && area.height > 0) {
            int largura = Math.min(tamanho.width, area.width);
            int altura = Math.min(tamanho.height, area.height);
            frame.setBounds((area.width - largura) / 2, (area.height - altura) / 2, largura, altura);
        }
        frame.setVisible(true);
        UtilSwing.trazerParaFrente(frame);
    }

    @Override
    public void setUsuarioAtual(String identificacao) {
        lblUsuario.setText("Usuário: " + identificacao);
    }

    @Override
    public void setOperacoesDisponiveis(boolean disponiveis) {
        mnuOperacao.setVisible(disponiveis);
        mnuDados.setVisible(disponiveis);
        pnlAcessoRapido.setVisible(disponiveis);
    }

    @Override
    public void setManutencaoUsuariosDisponivel(boolean disponivel) {
        mnuUsuarios.setVisible(disponivel);
    }

    @Override
    public void setAcaoIncluirProdutos(Runnable acao) {
        acaoIncluirProdutos = acao;
    }

    @Override
    public void setAcaoBuscarProdutos(Runnable acao) {
        acaoBuscarProdutos = acao;
    }

    @Override
    public void setAcaoCategorias(Runnable acao) {
        acaoCategorias = acao;
    }

    @Override
    public void setAcaoCalcularMargem(Runnable acao) {
        acaoCalcularMargem = acao;
    }

    @Override
    public void setAcaoClientes(Runnable acao) {
        acaoClientes = acao;
    }

    @Override
    public void setAcaoUsuarios(Runnable acao) {
        acaoUsuarios = acao;
    }

    @Override
    public void setAcaoSair(Runnable acao) {
        acaoSair = acao;
    }
}
