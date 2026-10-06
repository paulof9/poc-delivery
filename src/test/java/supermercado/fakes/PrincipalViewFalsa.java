package supermercado.fakes;

import supermercado.view.JanelaView;
import supermercado.view.PrincipalView;
import java.util.ArrayList;
import java.util.List;

public class PrincipalViewFalsa implements PrincipalView {

    public final List<JanelaView> janelasAbertas = new ArrayList<>();
    public boolean exibida;
    public boolean sessaoFechada;
    public String usuarioAtual;
    public boolean operacoesDisponiveis;
    public boolean manutencaoUsuariosDisponivel;
    public Runnable acaoIncluirProdutos;
    public Runnable acaoBuscarProdutos;
    public Runnable acaoCategorias;
    public Runnable acaoCalcularMargem;
    public Runnable acaoClientes;
    public Runnable acaoUsuarios;
    public Runnable acaoSair;

    public JanelaView ultimaJanela() {
        return janelasAbertas.isEmpty() ? null : janelasAbertas.get(janelasAbertas.size() - 1);
    }

    @Override
    public void exibir() {
        exibida = true;
    }

    @Override
    public void fecharSessao() {
        sessaoFechada = true;
    }

    @Override
    public void abrirJanela(JanelaView janela) {
        janelasAbertas.add(janela);
    }

    @Override
    public void setUsuarioAtual(String identificacao) {
        usuarioAtual = identificacao;
    }

    @Override
    public void setOperacoesDisponiveis(boolean disponiveis) {
        operacoesDisponiveis = disponiveis;
    }

    @Override
    public void setManutencaoUsuariosDisponivel(boolean disponivel) {
        manutencaoUsuariosDisponivel = disponivel;
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
