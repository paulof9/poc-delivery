package supermercado.fakes;

import supermercado.view.JanelaView;
import java.util.ArrayList;
import java.util.List;

public abstract class JanelaFalsa implements JanelaView {

    public final List<String> sucessos = new ArrayList<>();
    public final List<String> avisos = new ArrayList<>();
    public final List<String> erros = new ArrayList<>();
    public final List<String> confirmacoesSolicitadas = new ArrayList<>();
    public boolean respostaConfirmacao = true;
    public boolean aberta = true;
    private Runnable acaoFechar;

    public void clicarFechar() {
        acaoFechar.run();
    }

    @Override
    public void fechar() {
        aberta = false;
    }

    @Override
    public boolean estaAberta() {
        return aberta;
    }

    @Override
    public void trazerParaFrente() {
    }

    @Override
    public void setAcaoFechar(Runnable acao) {
        acaoFechar = acao;
    }

    @Override
    public void mostrarSucesso(String mensagem) {
        sucessos.add(mensagem);
    }

    @Override
    public void mostrarAviso(String mensagem) {
        avisos.add(mensagem);
    }

    @Override
    public void mostrarErro(String mensagem) {
        erros.add(mensagem);
    }

    @Override
    public boolean confirmar(String titulo, String mensagem) {
        confirmacoesSolicitadas.add(mensagem);
        return respostaConfirmacao;
    }
}
