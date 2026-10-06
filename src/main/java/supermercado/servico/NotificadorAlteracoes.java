package supermercado.servico;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificadorAlteracoes {

    private final List<ObservadorAlteracao> observadores = new CopyOnWriteArrayList<>();

    public void registrar(ObservadorAlteracao observador) {
        observadores.add(Objects.requireNonNull(observador, "observador"));
    }

    public void remover(ObservadorAlteracao observador) {
        observadores.remove(observador);
    }

    public void notificar(TipoAlteracao tipo) {
        for (ObservadorAlteracao observador : observadores) {
            observador.dadosAlterados(tipo);
        }
    }
}
