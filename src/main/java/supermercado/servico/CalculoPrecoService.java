package supermercado.servico;

import supermercado.excecao.RegraNegocioException;
import supermercado.excecao.ValidacaoException;
import supermercado.model.Categoria;
import supermercado.model.HistoricoPreco;
import supermercado.model.Produto;
import supermercado.repositorio.CategoriaRepository;
import supermercado.repositorio.HistoricoPrecoRepository;
import supermercado.repositorio.ProdutoRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CalculoPrecoService {

    public static final int INTERVALO_MINIMO_DIAS = 10;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final HistoricoPrecoRepository historicoRepository;
    private final NotificadorAlteracoes notificador;

    public CalculoPrecoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository,
            HistoricoPrecoRepository historicoRepository, NotificadorAlteracoes notificador) {
        this.produtoRepository = Objects.requireNonNull(produtoRepository);
        this.categoriaRepository = Objects.requireNonNull(categoriaRepository);
        this.historicoRepository = Objects.requireNonNull(historicoRepository);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public double calcularPrecoVenda(double precoCusto, double percentualLucro) {
        BigDecimal fator = BigDecimal.ONE.add(BigDecimal.valueOf(percentualLucro).movePointLeft(2));
        return BigDecimal.valueOf(precoCusto)
                .multiply(fator)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public Optional<LocalDate> buscarDataUltimoCalculo() {
        return historicoRepository.buscarDataUltimoCalculo();
    }

    public List<ResultadoCalculo> calcular(LocalDate dataCalculo) throws ValidacaoException, RegraNegocioException {
        if (dataCalculo == null) {
            throw new ValidacaoException("A data do cálculo é obrigatória.");
        }
        validarIntervalo(dataCalculo);

        List<Produto> produtos = produtoRepository.buscarTodos();
        if (produtos.isEmpty()) {
            throw new RegraNegocioException("Não há produtos cadastrados para calcular.");
        }

        List<ResultadoCalculo> resultados = new ArrayList<>();
        for (Produto produto : produtos) {
            Categoria categoria = categoriaRepository.buscarPorId(produto.getCategoria().getId())
                    .orElseThrow(() -> new RegraNegocioException("A categoria do produto \""
                            + produto.getNome() + "\" não existe no cadastro."));
            double percentual = categoria.getPercentualLucro();
            double precoVenda = calcularPrecoVenda(produto.getPrecoCusto(), percentual);
            resultados.add(new ResultadoCalculo(produto.getId(), produto.getNome(), produto.getPrecoCusto(),
                    categoria.getNome(), percentual, precoVenda));
        }

        for (int i = 0; i < produtos.size(); i++) {
            Produto produto = produtos.get(i);
            ResultadoCalculo resultado = resultados.get(i);
            produto.registrarCalculo(resultado.percentualLucro(), resultado.precoVenda());
            produtoRepository.salvar(produto);
            historicoRepository.salvar(new HistoricoPreco(produto.getId(), dataCalculo,
                    resultado.percentualLucro(), resultado.precoVenda()));
        }
        notificador.notificar(TipoAlteracao.PRECOS);
        return resultados;
    }

    private void validarIntervalo(LocalDate dataCalculo) throws RegraNegocioException {
        Optional<LocalDate> ultimoCalculo = historicoRepository.buscarDataUltimoCalculo();
        if (ultimoCalculo.isEmpty()) {
            return;
        }
        LocalDate proximoPermitido = ultimoCalculo.get().plusDays(INTERVALO_MINIMO_DIAS);
        if (dataCalculo.isBefore(proximoPermitido)) {
            throw new RegraNegocioException("O novo cálculo ainda não pode ser realizado.\n"
                    + "O último cálculo foi feito em " + ultimoCalculo.get().format(FORMATO_DATA)
                    + " e um novo cálculo só é permitido a partir de "
                    + proximoPermitido.format(FORMATO_DATA) + ".");
        }
    }
}
