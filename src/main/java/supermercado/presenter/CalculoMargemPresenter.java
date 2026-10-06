package supermercado.presenter;

import supermercado.excecao.NegocioException;
import supermercado.servico.CalculoPrecoService;
import supermercado.servico.ResultadoCalculo;
import supermercado.view.CalculoMargemView;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CalculoMargemPresenter {

    private final CalculoMargemView view;
    private final CalculoPrecoService calculoPrecoService;

    public CalculoMargemPresenter(CalculoMargemView view, CalculoPrecoService calculoPrecoService) {
        this(view, calculoPrecoService, Clock.systemDefaultZone());
    }

    public CalculoMargemPresenter(CalculoMargemView view, CalculoPrecoService calculoPrecoService, Clock relogio) {
        this.view = Objects.requireNonNull(view);
        this.calculoPrecoService = Objects.requireNonNull(calculoPrecoService);

        view.setAcaoCalcular(this::calcular);
        view.setAcaoFechar(view::fechar);
        view.setDataCalculo(Formatador.formatarData(LocalDate.now(relogio)));
    }

    private void calcular() {
        try {
            LocalDate data = Formatador.lerData(view.getDataCalculo(), "Data do cálculo");
            List<ResultadoCalculo> resultados = calculoPrecoService.calcular(data);
            view.setResultados(resultados.stream()
                    .map(resultado -> new String[]{
                        resultado.nomeProduto(),
                        Formatador.formatarMoeda(resultado.precoCusto()),
                        resultado.nomeCategoria(),
                        Formatador.formatarDecimal(resultado.percentualLucro()),
                        Formatador.formatarMoeda(resultado.precoVenda())
                    })
                    .toList());
            view.mostrarSucesso("Cálculo realizado com sucesso para " + resultados.size() + " produtos.");
        } catch (NegocioException e) {
            view.mostrarAviso(e.getMessage());
        }
    }
}
