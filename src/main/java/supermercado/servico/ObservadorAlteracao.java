package supermercado.servico;

@FunctionalInterface
public interface ObservadorAlteracao {

    void dadosAlterados(TipoAlteracao tipo);
}
