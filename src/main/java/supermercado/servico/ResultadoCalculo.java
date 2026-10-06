package supermercado.servico;

public record ResultadoCalculo(
        Long produtoId,
        String nomeProduto,
        Double precoCusto,
        String nomeCategoria,
        Double percentualLucro,
        Double precoVenda) {
}
