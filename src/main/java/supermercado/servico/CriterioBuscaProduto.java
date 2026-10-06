package supermercado.servico;

public enum CriterioBuscaProduto {
    NOME("Nome do produto"),
    CATEGORIA("Categoria");

    private final String descricao;

    CriterioBuscaProduto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
