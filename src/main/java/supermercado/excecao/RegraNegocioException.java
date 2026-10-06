package supermercado.excecao;

public class RegraNegocioException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
