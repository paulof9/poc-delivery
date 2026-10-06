package supermercado.excecao;

public class ValidacaoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
